package com.mps.acessos.infrastructure;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.mps.acessos.domain.AcessoLog;
import com.mps.acessos.domain.IAcessoLogRepository;
import com.mps.shared.exception.RepositorioException;
import com.mps.shared.infrastructure.HibernateSessionFactory;
import com.mps.shared.logging.AppLoggerFactory;
import com.mps.shared.logging.Logger;

public class HibernateAcessoLogRepository implements IAcessoLogRepository {

    private static final Logger LOGGER = AppLoggerFactory.getLogger(HibernateAcessoLogRepository.class);

    private final SessionFactory sessionFactory;

    public HibernateAcessoLogRepository() {
        this.sessionFactory = HibernateSessionFactory.getInstance();
    }

    @Override
    public void salvar(AcessoLog acessoLog) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(acessoLog);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOGGER.error("Erro ao salvar log de acesso no banco de dados", e);
            throw new RepositorioException("Erro ao salvar log de acesso no banco de dados", e);
        }
    }

    @Override
    public List<AcessoLog> buscarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from AcessoLog", AcessoLog.class).list();
        } catch (Exception e) {
            LOGGER.error("Erro ao buscar logs de acesso do banco de dados", e);
            throw new RepositorioException("Erro ao buscar logs de acesso do banco de dados", e);
        }
    }
}
