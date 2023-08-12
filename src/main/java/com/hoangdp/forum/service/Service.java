package com.hoangdp.forum.service;

import java.lang.annotation.Annotation;
import java.sql.Date;
import java.util.List;

import org.hibernate.Session;

import com.hoangdp.forum.entity.BaseEntity;
import com.hoangdp.forum.entity.User;
import com.hoangdp.forum.utils.HibernateUtils;

import jakarta.persistence.Table;

public class Service<T extends BaseEntity, V> {

    private final Class<T> entityClass;

    public Service(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public List<T> findAll() {
        try (Session s = HibernateUtils.getSessionFactory().openSession()) {
            return s.createNativeQuery("select * from " + getNameTable(), entityClass).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public T create(T t) {
        try (Session s = HibernateUtils.getSessionFactory().openSession()) {
            // Pre -handle data

            User u = UserService.getInstant().getCurrentUser();

            t.setCreateOn(new Date(System.currentTimeMillis()));
            t.setCreatedBy(u.getId());

            t.setLastModifiedOn(new Date(System.currentTimeMillis()));
            t.setLastModifiedBy(u.getId());

            s.beginTransaction();
            s.persist(t);
            s.flush();
            s.getTransaction().commit();
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public T update(V id, T t) {
        try (Session s = HibernateUtils.getSessionFactory().openSession()) {
            T oldT = findById(id);

            User u = UserService.getInstant().getCurrentUser();
            t.setCreateOn(oldT.getCreateOn());
            t.setCreatedBy(oldT.getCreatedBy());
            t.setLastModifiedBy(u.getLastModifiedBy());
            t.setLastModifiedOn(u.getLastModifiedOn());

            s.beginTransaction();
            s.persist(t);
            s.flush();
            s.getTransaction().commit();
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteById(V id) {
        try (Session s = HibernateUtils.getSessionFactory().openSession()) {
            s.beginTransaction();
            T t = s.get(entityClass, id);
            s.remove(t);
            s.getTransaction().commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public T findById(V id) {
        try (Session s = HibernateUtils.getSessionFactory().openSession()) {
            s.beginTransaction();
            return s.find(entityClass, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Long count() {
        try (Session s = HibernateUtils.getSessionFactory().openSession()) {
            s.beginTransaction();
            return s.createNamedQuery("select count(*) from " + getNameTable(), Long.class).getSingleResult();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String getNameTable() {
        for (Annotation a : entityClass.getAnnotations()) {
            if (a instanceof Table) {
                return ((Table) a).name();
            }
        }
        return entityClass.getSimpleName();
    }
}
