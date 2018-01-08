/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.exceptions.NonexistentEntityException;

/**
 *
 * @author juanm
 */
public class ScoreJpaController implements Serializable {

    public ScoreJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Score score) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Configuration configId = score.getConfigId();
            if (configId != null) {
                configId = em.getReference(configId.getClass(), configId.getConfigId());
                score.setConfigId(configId);
            }
            em.persist(score);
            if (configId != null) {
                configId.getScoreList().add(score);
                configId = em.merge(configId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Score score) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Score persistentScore = em.find(Score.class, score.getScoreId());
            Configuration configIdOld = persistentScore.getConfigId();
            Configuration configIdNew = score.getConfigId();
            if (configIdNew != null) {
                configIdNew = em.getReference(configIdNew.getClass(), configIdNew.getConfigId());
                score.setConfigId(configIdNew);
            }
            score = em.merge(score);
            if (configIdOld != null && !configIdOld.equals(configIdNew)) {
                configIdOld.getScoreList().remove(score);
                configIdOld = em.merge(configIdOld);
            }
            if (configIdNew != null && !configIdNew.equals(configIdOld)) {
                configIdNew.getScoreList().add(score);
                configIdNew = em.merge(configIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = score.getScoreId();
                if (findScore(id) == null) {
                    throw new NonexistentEntityException("The score with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Score score;
            try {
                score = em.getReference(Score.class, id);
                score.getScoreId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The score with id " + id + " no longer exists.", enfe);
            }
            Configuration configId = score.getConfigId();
            if (configId != null) {
                configId.getScoreList().remove(score);
                configId = em.merge(configId);
            }
            em.remove(score);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Score> findScoreEntities() {
        return findScoreEntities(true, -1, -1);
    }

    public List<Score> findScoreEntities(int maxResults, int firstResult) {
        return findScoreEntities(false, maxResults, firstResult);
    }

    private List<Score> findScoreEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Score.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Score findScore(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Score.class, id);
        } finally {
            em.close();
        }
    }

    public int getScoreCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Score> rt = cq.from(Score.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
