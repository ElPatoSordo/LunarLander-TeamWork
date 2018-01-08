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
public class UsersJpaController implements Serializable {

    public UsersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) {
        if (users.getConfigurationList() == null) {
            users.setConfigurationList(new ArrayList<Configuration>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Configuration> attachedConfigurationList = new ArrayList<Configuration>();
            for (Configuration configurationListConfigurationToAttach : users.getConfigurationList()) {
                configurationListConfigurationToAttach = em.getReference(configurationListConfigurationToAttach.getClass(), configurationListConfigurationToAttach.getConfigId());
                attachedConfigurationList.add(configurationListConfigurationToAttach);
            }
            users.setConfigurationList(attachedConfigurationList);
            em.persist(users);
            for (Configuration configurationListConfiguration : users.getConfigurationList()) {
                Users oldUserIdOfConfigurationListConfiguration = configurationListConfiguration.getUserId();
                configurationListConfiguration.setUserId(users);
                configurationListConfiguration = em.merge(configurationListConfiguration);
                if (oldUserIdOfConfigurationListConfiguration != null) {
                    oldUserIdOfConfigurationListConfiguration.getConfigurationList().remove(configurationListConfiguration);
                    oldUserIdOfConfigurationListConfiguration = em.merge(oldUserIdOfConfigurationListConfiguration);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getUserId());
            List<Configuration> configurationListOld = persistentUsers.getConfigurationList();
            List<Configuration> configurationListNew = users.getConfigurationList();
            List<String> illegalOrphanMessages = null;
            for (Configuration configurationListOldConfiguration : configurationListOld) {
                if (!configurationListNew.contains(configurationListOldConfiguration)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Configuration " + configurationListOldConfiguration + " since its userId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Configuration> attachedConfigurationListNew = new ArrayList<Configuration>();
            for (Configuration configurationListNewConfigurationToAttach : configurationListNew) {
                configurationListNewConfigurationToAttach = em.getReference(configurationListNewConfigurationToAttach.getClass(), configurationListNewConfigurationToAttach.getConfigId());
                attachedConfigurationListNew.add(configurationListNewConfigurationToAttach);
            }
            configurationListNew = attachedConfigurationListNew;
            users.setConfigurationList(configurationListNew);
            users = em.merge(users);
            for (Configuration configurationListNewConfiguration : configurationListNew) {
                if (!configurationListOld.contains(configurationListNewConfiguration)) {
                    Users oldUserIdOfConfigurationListNewConfiguration = configurationListNewConfiguration.getUserId();
                    configurationListNewConfiguration.setUserId(users);
                    configurationListNewConfiguration = em.merge(configurationListNewConfiguration);
                    if (oldUserIdOfConfigurationListNewConfiguration != null && !oldUserIdOfConfigurationListNewConfiguration.equals(users)) {
                        oldUserIdOfConfigurationListNewConfiguration.getConfigurationList().remove(configurationListNewConfiguration);
                        oldUserIdOfConfigurationListNewConfiguration = em.merge(oldUserIdOfConfigurationListNewConfiguration);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = users.getUserId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
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
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getUserId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Configuration> configurationListOrphanCheck = users.getConfigurationList();
            for (Configuration configurationListOrphanCheckConfiguration : configurationListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the Configuration " + configurationListOrphanCheckConfiguration + " in its configurationList field has a non-nullable userId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
