package com.mps.produtos.infrastructure;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.mps.produtos.domain.IProdutoRepository;
import com.mps.produtos.domain.Produto;
import com.mps.shared.exception.RepositorioException;
import com.mps.shared.infrastructure.HibernateSessionFactory;
import com.mps.shared.logging.AppLoggerFactory;
import com.mps.shared.logging.Logger;

public class HibernateProdutoRepository implements IProdutoRepository {

    private static final Logger LOGGER = AppLoggerFactory.getLogger(HibernateProdutoRepository.class);

    private final SessionFactory sessionFactory;

    public HibernateProdutoRepository() {
        this.sessionFactory = HibernateSessionFactory.getInstance();
    }

    @Override
    public void salvar(Produto produto) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(produto);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOGGER.error("Erro ao salvar produto no banco de dados", e);
            throw new RepositorioException("Erro ao salvar produto no banco de dados", e);
        }
    }

    @Override
    public List<Produto> buscarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Produto", Produto.class).list();
        } catch (Exception e) {
            LOGGER.error("Erro ao buscar produtos do banco de dados", e);
            throw new RepositorioException("Erro ao buscar produtos do banco de dados", e);
        }
    }

    @Override
    public Optional<Produto> buscarPorId(UUID id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Produto.class, id));
        } catch (Exception e) {
            LOGGER.error("Erro ao buscar produto no banco de dados", e);
            throw new RepositorioException("Erro ao buscar produto no banco de dados", e);
        }
    }

    @Override
    public Produto atualizar(Produto produto) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Produto atualizado = session.merge(produto);
            tx.commit();
            return atualizado;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOGGER.error("Erro ao atualizar produto no banco de dados", e);
            throw new RepositorioException("Erro ao atualizar produto no banco de dados", e);
        }
    }

    @Override
    public void deletar(UUID id) {
        alterarAtivo(id, false, "Erro ao remover produto no banco de dados");
    }

    @Override
    public void reativar(UUID id) {
        alterarAtivo(id, true, "Erro ao reativar produto no banco de dados");
    }

    private void alterarAtivo(UUID id, boolean ativo, String mensagemErro) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Produto produto = session.find(Produto.class, id);
            if (produto == null) {
                throw new RepositorioException("Produto não encontrado");
            }
            produto.setAtivo(ativo);
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
