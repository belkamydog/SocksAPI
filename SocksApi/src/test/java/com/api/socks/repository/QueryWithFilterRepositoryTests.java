package com.api.socks.repository;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class QueryWithFilterRepositoryTests {


    @Test
    public void queryOneColorFilterTest(){
        Assertions.assertEquals("color in (red)", QueryWithFilterRepository.addColorFilter("red"));
    }

    @Test
    public void queryTwoColorFilterTest(){
        Assertions.assertEquals("color in (red,black)", QueryWithFilterRepository.addColorFilter("red,black"));
    }
    @Test
    public void queryManyColorsFilterTest(){
        Assertions.assertEquals("color in (red,black,white,yellow)", QueryWithFilterRepository.addColorFilter("red,black,white,yellow"));
    }
    @Test
    public void queryOneCottonConditionFilterTest(){
        Assertions.assertEquals("cotton < 5 ", QueryWithFilterRepository.addCottonFilter("lt:5", false));
    }

    @Test
    public void queryTwoCottonConditionFilterTest(){
        Assertions.assertEquals("cotton < 5 and cotton > 9 ", QueryWithFilterRepository.addCottonFilter("lt:5 and gt:9", false));
    }

    @Test
    public void queryTwoCottonConditionEqualFilterTest(){
        Assertions.assertEquals("cotton = 5 and cotton = 9 ", QueryWithFilterRepository.addCottonFilter("eq:5 and eq:9", false));
    }

    @Test
    public void queryTwoCottonConditionEqualWithAndFilterTest(){
        Assertions.assertEquals(" and cotton = 5 and cotton = 9 ", QueryWithFilterRepository.addCottonFilter("eq:5 and eq:9", true));
    }

    @Test
    public void queryCreationFullWithoutCottonTest(){
        Map<String, String> filter = new HashMap<>();
        filter.put("color", "white");
        Assertions.assertEquals("select sum(x.count) from Socks x where color in (white)", QueryWithFilterRepository.socksGetCountQueryCreator(filter));
    }

    @Test
    public void queryCreationFullTest(){
        Map<String, String> filter = new HashMap<>();
        filter.put("color", "white");
        filter.put("cotton", "eq:45");
        Assertions.assertEquals("select sum(x.count) from Socks x where color in (white) and cotton = 45 ", QueryWithFilterRepository.socksGetCountQueryCreator(filter));
    }

    @Test
    public void queryCreationFullCottonTwoConditionsTest(){
        Map<String, String> filter = new HashMap<>();
        filter.put("color", "white");
        filter.put("cotton", "lt:45 and gt:30");
        Assertions.assertEquals("select sum(x.count) from Socks x where color in (white) and cotton < 45 and cotton > 30 ", QueryWithFilterRepository.socksGetCountQueryCreator(filter));
    }
}
