package com.mps.shared.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.mps.acessos.domain.AcessoLog;
import com.mps.anuncios.domain.Anuncio;
import com.mps.produtos.domain.Produto;
import com.mps.shared.exception.RepositorioException;
import com.mps.users.domain.User;

public final class HibernateSessionFactory {

    private static SessionFactory instance;

    private HibernateSessionFactory() {
    }

    public static synchronized SessionFactory getInstance() {
        if (instance == null) {
            instance = criar();
        }
        return instance;
    }

    private static SessionFactory criar() {
        try {
            Properties props = carregarPropriedades();
            Configuration config = new Configuration();
            config.setProperties(props);
            config.addAnnotatedClass(User.class);
            config.addAnnotatedClass(Produto.class);
            config.addAnnotatedClass(Anuncio.class);
            config.addAnnotatedClass(AcessoLog.class);
            return config.buildSessionFactory();
        } catch (IOException e) {
            throw new RepositorioException("Falha ao ler application.properties", e);
        } catch (Exception e) {
            throw new RepositorioException("Falha ao inicializar o banco de dados", e);
        }
    }

    private static Properties carregarPropriedades() throws IOException {
        Properties props = new Properties();
        try (InputStream is = HibernateSessionFactory.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (is == null) {
                throw new IOException("Arquivo application.properties não encontrado no classpath");
            }
            props.load(is);
        }
        return props;
    }

    public static synchronized void fechar() {
        if (instance != null && !instance.isClosed()) {
            instance.close();
            instance = null;
        }
    }
}
