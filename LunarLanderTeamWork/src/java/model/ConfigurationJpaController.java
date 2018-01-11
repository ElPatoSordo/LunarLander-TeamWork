/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.exceptions.IllegalOrphanException;
import model.exceptions.NonexistentEntityException;

/**
 *
 * @author juanm
 */
public class ConfigurationJpaController implements Serializable {

    public ConfigurationJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
      public Boolean existByConfigName(String configname, Users user) {
        EntityManager em = getEntityManager();
        try {
            List<Users> list = em.createNamedQuery("Configuration.findByConfigNameAndUserId").setParameter("configName", configname).setParameter("userId", user).getResultList();
            return !list.isEmpty();

        } finally {
            em.close();
        }
    }

    public void create(Configuration configuration) {
        if (configuration.getScoreList() == null) {
            configuration.setScoreList(new ArrayList<Score>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users userId = configuration.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getUserId());
                configuration.setUserId(userId);
            }
            List<Score> attachedScoreList = new ArrayList<Score>();
            for (Score scoreListScoreToAttach : configuration.getScoreList()) {
                scoreListScoreToAttach = em.getReference(scoreListScoreToAttach.getClass(), scoreListScoreToAttach.getScoreId());
                attachedScoreList.add(scoreListScoreToAttach);
            }
            configuration.setScoreList(attachedScoreList);
            em.persist(configuration);
            if (userId != null) {
                userId.getConfigurationList().add(configuration);
                userId = em.merge(userId);
            }
            for (Score scoreListScore : configuration.getScoreList()) {
                Configuration oldConfigIdOfScoreListScore = scoreListScore.getConfigId();
                scoreListScore.setConfigId(configuration);
                scoreListScore = em.merge(scoreListScore);
                if (oldConfigIdOfScoreListScore != null) {
                    oldConfigIdOfScoreListScore.getScoreList().remove(scoreListScore);
                    oldConfigIdOfScoreListScore = em.merge(oldConfigIdOfScoreListScore);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Configuration configuration) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Configuration persistentConfiguration = em.find(Configuration.class, configuration.getConfigId());
            Users userIdOld = persistentConfiguration.getUserId();
            Users userIdNew = configuration.getUserId();
            List<Score> scoreListOld = persistentConfiguration.getScoreList();
            List<Score> scoreListNew = configuration.getScoreList();
            List<String> illegalOrphanMessages = null;
            for (Score scoreListOldScore : scoreListOld) {
                if (!scoreListNew.contains(scoreListOldScore)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Score " + scoreListOldScore + " since its configId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getUserId());
                configuration.setUserId(userIdNew);
            }
            List<Score> attachedScoreListNew = new ArrayList<Score>();
            for (Score scoreListNewScoreToAttach : scoreListNew) {
                scoreListNewScoreToAttach = em.getReference(scoreListNewScoreToAttach.getClass(), scoreListNewScoreToAttach.getScoreId());
                attachedScoreListNew.add(scoreListNewScoreToAttach);
            }
            scoreListNew = attachedScoreListNew;
            configuration.setScoreList(scoreListNew);
            configuration = em.merge(configuration);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getConfigurationList().remove(configuration);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getConfigurationList().add(configuration);
                userIdNew = em.merge(userIdNew);
            }
            for (Score scoreListNewScore : scoreListNew) {
                if (!scoreListOld.contains(scoreListNewScore)) {
                    Configuration oldConfigIdOfScoreListNewScore = scoreListNewScore.getConfigId();
                    scoreListNewScore.setConfigId(configuration);
                    scoreListNewScore = em.merge(scoreListNewScore);
                    if (oldConfigIdOfScoreListNewScore != null && !oldConfigIdOfScoreListNewScore.equals(configuration)) {
                        oldConfigIdOfScoreListNewScore.getScoreList().remove(scoreListNewScore);
                        oldConfigIdOfScoreListNewScore = em.merge(oldConfigIdOfScoreListNewScore);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = configuration.getConfigId();
                if (findConfiguration(id) == null) {
                    throw new NonexistentEntityException("The configuration with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Configuration configuration;
            try {
                configuration = em.getReference(Configuration.class, id);
                configuration.getConfigId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The configuration with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Score> scoreListOrphanCheck = configuration.getScoreList();
            for (Score scoreListOrphanCheckScore : scoreListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Configuration (" + configuration + ") cannot be destroyed since the Score " + scoreListOrphanCheckScore + " in its scoreList field has a non-nullable configId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Users userId = configuration.getUserId();
            if (userId != null) {
                userId.getConfigurationList().remove(configuration);
                userId = em.merge(userId);
            }
            em.remove(configuration);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Configuration> findConfigurationEntities() {
        return findConfigurationEntities(true, -1, -1);
    }

    public List<Configuration> findConfigurationEntities(int maxResults, int firstResult) {
        return findConfigurationEntities(false, maxResults, firstResult);
    }

    private List<Configuration> findConfigurationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Configuration.class));
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

    public Configuration findConfiguration(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Configuration.class, id);
        } finally {
            em.close();
        }
    }

    public int getConfigurationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Configuration> rt = cq.from(Configuration.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
