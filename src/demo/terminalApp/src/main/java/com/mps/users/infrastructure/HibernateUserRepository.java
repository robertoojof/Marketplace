package com.mps.users.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.mps.shared.exception.RepositorioException;
import com.mps.users.domain.IUserRepository;
import com.mps.users.domain.User;

public class HibernateUserRepository implements IUserRepository {

    private final SessionFactory sessionFactory;

    public HibernateUserRepository() {
        try {
            Properties props = carregarPropriedades();
            Configuration config = new Configuration();
            config.setProperties(props);
            config.addAnnotatedClass(User.class);
            this.sessionFactory = config.buildSessionFactory();
        } catch (IOException e) {
            throw new RepositorioException("Falha ao ler application.properties", e);
        } catch (Exception e) {
            throw new RepositorioException("Falha ao inicializar o banco de dados", e);
        }
    }

    private Properties carregarPropriedades() throws IOException {
        Properties props = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (is == null) {
                throw new IOException("Arquivo application.properties não encontrado no classpath");
            }
            props.load(is);
        }
        return props;
    }

    @Override
    public void salvar(User user) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RepositorioException("Erro ao salvar usuário no banco de dados", e);
        }
    }

    @Override
    public List<User> buscarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            throw new RepositorioException("Erro ao buscar usuários do banco de dados", e);
        }
    }

    @Override
    public Optional<User> buscarPorId(UUID id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(User.class, id));
        } catch (Exception e) {
            throw new RepositorioException("Erro ao buscar usuário no banco de dados", e);
        }
    }

    @Override
    public User atualizar(User user) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            User atualizado = session.merge(user);
            tx.commit();
            return atualizado;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RepositorioException("Erro ao atualizar usuário no banco de dados", e);
        }
    }

    @Override
    public void deletar(UUID id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            User usuario = session.find(User.class, id);
            if (usuario == null) {
                throw new RepositorioException("Usuário não encontrado");
            }
            usuario.setAtivo(false);
            tx.commit();
        } catch (RepositorioException e) {
            if (tx != null) tx.rollback();
            throw e;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RepositorioException("Erro ao remover usuário no banco de dados", e);
        }
    }

    public void fechar() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
