package com.api.socks.repository;

import com.api.socks.models.Socks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SocksRepositoryCrud extends CrudRepository <Socks, String> {

    @Query("select s from Socks s where s.color = :clr and s.cotton = :ctn")
    public Optional<Socks> getSocksByColorAndCotton(@Param("clr") Socks.SocksColors color,
                                                    @Param("ctn") int cotton);
}