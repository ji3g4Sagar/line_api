package com.example.line.demo.common;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
    // private static final EntityManagerFactory entityManagerFactory;

    // static {
    //     try {
    //         entityManagerFactory = 
    //             Persistence.createEntityManagerFactory("dev");
    //     }
    //     catch(Throwable ex) {
    //         throw new ExceptionInInitializerError(ex);
    //     }
    // }
 
    // public static EntityManagerFactory getEntityManagerFactory() {
    //     return entityManagerFactory;
    // }
 
    // public static void shutdown() {
    //     getEntityManagerFactory().close();
    // }
    private EntityManagerFactory emfInstance;

    private static JPAUtil emf;

    private JPAUtil() {
    }

    public EntityManagerFactory get() {
        if(emfInstance == null) {
            emfInstance = Persistence.createEntityManagerFactory("dev");
        }
        return emfInstance;
    }

    public static JPAUtil getInstance() {
        if(emf == null) {
            emf = new JPAUtil();
        }
        return emf;
    }
}