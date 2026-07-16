package com.mps.anuncios.infrastructure;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.mps.anuncios.domain.Anuncio;
import com.mps.anuncios.domain.IAnuncioRepository;
import com.mps.shared.exception.RepositorioException;
import com.mps.shared.infrastructure.HibernateSessionFactory;

public class HibernateAnuncioRepository implements IAnuncioRepository {

    private final SessionFactory sessionFactory;

    public HibernateAnuncioRepository() {
        this.sessionFactory = HibernateSessionFactory.getInstance();
    }

    @Override
    public void salvar(Anuncio anuncio) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(anuncio);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RepositorioException("Erro ao salvar anúncio no banco de dados", e);
        }
    }

    @Override
    public List<Anuncio> buscarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Anuncio", Anuncio.class).list();
        } catch (Exception e) {
            throw new RepositorioException("Erro ao buscar anúncios do banco de dados", e);
        }
    }

    @Override
    public Optional<Anuncio> buscarPorId(UUID id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Anuncio.class, id));
        } catch (Exception e) {
            throw new RepositorioException("Erro ao buscar anúncio no banco de dados", e);
        }
    }

    @Override
    public List<Anuncio> buscarPorVendedor(UUID vendedorId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Anuncio where vendedor.id = :vendedorId", Anuncio.class)
                    .setParameter("vendedorId", vendedorId)
                    .list();
        } catch (Exception e) {
            throw new RepositorioException("Erro ao buscar anúncios do vendedor no banco de dados", e);
        }
    }

    @Override
    public Anuncio atualizar(Anuncio anuncio) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Anuncio atualizado = session.merge(anuncio);
            tx.commit();
            return atualizado;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RepositorioException("Erro ao atualizar anúncio no banco de dados", e);
        }
    }

    @Override
    public void deletar(UUID id) {
        alterarAtivo(id, false, "Erro ao remover anúncio no banco de dados");
    }

    @Override
    public void reativar(UUID id) {
        alterarAtivo(id, true, "Erro ao reativar anúncio no banco de dados");
    }

    @Override
    public void desativarTodosDoVendedor(UUID vendedorId) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.createMutationQuery("update Anuncio set ativo = false where vendedor.id = :vendedorId")
                    .setParameter("vendedorId", vendedorId)
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RepositorioException("Erro ao desativar anúncios do vendedor no banco de dados", e);
        }
    }

    private void alterarAtivo(UUID id, boolean ativo, String mensagemErro) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Anuncio anuncio = session.find(Anuncio.class, id);
            if (anuncio == null) {
                throw new RepositorioException("Anúncio não encontrado");
            }
            anuncio.setAtivo(ativo);
            tx.commit();
        } catch (RepositorioException e) {
            if (tx != null) tx.rollback();
            throw e;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RepositorioException(mensagemErro, e);
        }
    }
}
