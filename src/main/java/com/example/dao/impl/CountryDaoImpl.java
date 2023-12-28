package com.example.dao.impl;

import com.example.dao.CountryDao;
import com.example.entity.CountryEntity;
import com.example.exception.DAOException;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CountryDaoImpl implements CountryDao {

    private static final Logger logger = LogManager.getLogger(CountryDaoImpl.class);

    private final SessionFactory sessionFactory;

    @Autowired
    public CountryDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<CountryEntity> findAll() {

        Session session = null;
        List<CountryEntity> countries = new ArrayList<>();

        try {
            session = sessionFactory.openSession();

            String hql = "FROM CountryEntity";
            TypedQuery<CountryEntity> query = session.createQuery(hql, CountryEntity.class);

            countries = query.getResultList();

        } catch (Exception e) {
            throw new DAOException("Error occurred while retrieving all countries", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
            return countries;
        }
    }

    @Override
    public CountryEntity getById(Serializable id) {
        Session session = null;

        try {
            session = sessionFactory.openSession();

            String hql = "FROM CountryEntity c WHERE c.id = :countryId";

            return session.createQuery(hql, CountryEntity.class)
                    .setParameter("countryId", id)
                    .uniqueResult();

        } catch (Exception e) {
            throw new DAOException("Error occurred while retrieving entity by ID", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public Optional<CountryEntity> findById(Serializable id) {
        return Optional.ofNullable(getById(id));
    }

}
