package com.api.socks.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public class QueryWithFilterRepository {
    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    public QueryWithFilterRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static String addColorFilter(String colorFilter){
        StringBuilder result = new StringBuilder();
        if (!colorFilter.isEmpty())
            result.append("color in (").append(colorFilter).append(")");
        return  result.toString();
    }

    public static String addCottonFilter(String cottonFilter, boolean wasColor){
        StringBuilder result = new StringBuilder();
        if (wasColor) result.append(" and cotton ");
        else result.append("cotton ");
        String [] conditions = cottonFilter.split(" ");
        for (String i : conditions){
            String [] values =  i.split(":");
            for (String  k : values){
                switch (k) {
                    case "lt" -> result.append("< ");
                    case "gt" -> result.append("> ");
                    case "eq" -> result.append("= ");
                    default -> {
                        if (k.equals("and") || k.equals("or")) result.append(k).append(" cotton ");
                        else result.append(k).append(" ");
                    }
                }
            }
        }
        return  result.toString();
    }

    public static String socksGetCountQueryCreator(Map <String, String> filter){
        String colorFilter = filter.get("color");
        String cottonFilter = filter.get("cotton");
        StringBuilder query = new StringBuilder("select sum(x.count) from Socks x");
        if (colorFilter != null || cottonFilter != null){
            query.append(" where ");
            boolean flagFirstCondition = false;
            if (colorFilter != null){
                query.append(addColorFilter(colorFilter));
                flagFirstCondition = true;
            }
            if (cottonFilter != null) query.append(addCottonFilter(cottonFilter, flagFirstCondition));
        }
        return query.toString();
    }

    private String getAllCountOfSocks(){
        return "select sum(x.count) from Socks x";
    }

    public long getCountOfSocksByFilter(Map<String, String> filter){
        Query q = entityManager.createQuery(socksGetCountQueryCreator(filter));
        Long result  = (Long) q.getSingleResult();
        if (result == null) return 0;
        return result;
    }

    public long getCountOfSocksWithoutFilter(){
        Query q = entityManager.createQuery(getAllCountOfSocks());
        Long result  = (Long) q.getSingleResult();
        if (result == null) return 0;
        return result;
    }

}
