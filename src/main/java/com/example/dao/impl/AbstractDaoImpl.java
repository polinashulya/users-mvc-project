package com.example.dao.impl;

import com.example.dao.AbstractDao;
import com.example.entity.CountryEntity;
import com.example.entity.core.AbstractBaseEntity;
import com.example.exception.DAOException;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.function.Function;

@Repository
public abstract class AbstractDaoImpl<E extends AbstractBaseEntity> implements AbstractDao<E> {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    protected SessionFactory sessionFactory;
    private final Class<E> clazz;

    @SuppressWarnings("unchecked")
    public AbstractDaoImpl() {
        // Determine the class of E at runtime
        this.clazz = (Class<E>) ((ParameterizedType)
                this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    @Override
    public E getById(String sql, Long id) {
        return executeQuery(session ->
                session.createQuery(sql, clazz)
                        .setParameter("id", id)
                        .uniqueResult());
    }

//    public List<E> findAll(String sql) {
//        return executeQuery(session ->
//                session.createQuery(sql, clazz)
//                        .getResultList());
//    }

    protected <T> T executeQuery(Function<Session, T> queryFunction) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            return queryFunction.apply(session);
        } catch (Exception e) {
            throw new DAOException("Error occurred while executing query", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}