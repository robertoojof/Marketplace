package com.mps.users.infrastructure;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.mps.shared.exception.RepositorioException;
import com.mps.shared.infrastructure.HibernateSessionFactory;
import com.mps.shared.logging.AppLoggerFactory;
import com.mps.shared.logging.Logger;
import com.mps.users.domain.IUserRepository;
import com.mps.users.domain.User;

public class HibernateUserRepository implements IUserRepository {

    private static final Logger LOGGER = AppLoggerFactory.getLogger(HibernateUserRepository.class);

    private final SessionFactory sessionFactory;

    public HibernateUserRepository() {
        this.sessionFactory = HibernateSessionFactory.getInstance();
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
            LOGGER.error("Erro ao salvar usuário no banco de dados", e);
            throw new RepositorioException("Erro ao salvar usuário no banco de dados", e);
        }
    }

    @Override
    public List<User> buscarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            LOGGER.error("Erro ao buscar usuários do banco de dados", e);
            throw new RepositorioException("Erro ao buscar usuários do banco de dados", e);
        }
    }

    @Override
    public Optional<User> buscarPorId(UUID id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(User.class, id));
        } catch (Exception e) {
            LOGGER.error("Erro ao buscar usuário no banco de dados", e);
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
            LOGGER.error("Erro ao atualizar usuário no banco de dados", e);
            throw new RepositorioException("Erro ao atualizar usuário no banco de dados", e);
        }
    }

    @Override
    public Optional<User> buscarPorLogin(String login) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User where login = :login", User.class)
                    .setParameter("login", login)
                    .uniqueResultOptional();
        } catch (Exception e) {
            LOGGER.error("Erro ao buscar usuário no banco de dados", e);
            throw new RepositorioException("Erro ao buscar usuário no banco de dados", e);
        }
    }

    @Override
    public void deletar(UUID id) {
        alterarAtivo(id, false, "Erro ao remover usuário no banco de dados");
    }

    @Override
    public void reativar(UUID id) {
        alterarAtivo(id, true, "Erro ao reativar usuário no banco de dados");
    }

    private void alterarAtivo(UUID id, boolean ativo, String mensagemErro) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            User usuario = session.find(User.class, id);
            if (usuario == null) {
                throw new RepositorioException("Usuário não encontrado");
            }
            usuario.setAtivo(ativo);
            tx.commit();
        } catch (RepositorioException e) {
            if (tx != null) tx.rollback();
            LOGGER.error(mensagemErro, e);
            throw e;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOGGER.error(mensagemErro, e);
            throw new RepositorioException(mensagemErro, e);
        }
    }
}
