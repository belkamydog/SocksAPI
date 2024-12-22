package com.api.socks.service;

import com.api.socks.exception.InvalidCsvException;
import com.api.socks.exceptions.LackOfSocksException;
import com.api.socks.models.Socks;
import com.api.socks.repository.QueryWithFilterRepository;
import com.api.socks.repository.SocksRepositoryCrud;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RepositoryService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SocksRepositoryCrud socksRepository;
    private final QueryWithFilterRepository repositorySocks;

    public RepositoryService(SocksRepositoryCrud socksRepository, QueryWithFilterRepository repositorySocks){
        this.socksRepository = socksRepository;
        this.repositorySocks = repositorySocks;
    }

    public void addSocks(Socks socks){
        Optional <Socks> needs = socksRepository.getSocksByColorAndCotton(socks.getColor(), socks.getCotton());
        if (needs.isPresent()){
            Socks tmp = needs.get();
            tmp.setCount(tmp.getCount() + socks.getCount());
            socksRepository.save(tmp);
        }
        else socksRepository.save(socks);
        log.info("ADD " + socks);
    }

    public void removeSocks(Socks socks) throws LackOfSocksException {
        Optional <Socks> needs = socksRepository.getSocksByColorAndCotton(socks.getColor(), socks.getCotton());
        if (needs.isPresent()){
            Socks tmp = needs.get();
            if (tmp.getCount() >= socks.getCount()){
                tmp.setCount(tmp.getCount() - socks.getCount());
                socksRepository.save(tmp);
            } else {
                log.error("CAN'T REMOVE " + socks.getCount() + " SOCKS WITH ID " + socks.getId());
                throw new LackOfSocksException();
            }
        } else{
            log.error("CAN'T FIND SOCKS WITH ID " + socks.getId() + " FOR REMOVE");
            throw new NoSuchElementException();
        }
        log.info("REMOVE " + socks.getCount() + " SOCKS WITH ID " + socks.getId());
    }

    public void update(long id, Socks actual){
        Optional<Socks> socks = socksRepository.findById(Long.toString(id));
        if (socks.isPresent()) {
            Socks needUpdate = socks.get();
            needUpdate.setCount(actual.getCount());
            needUpdate.setColor(actual.getColor());
            needUpdate.setCotton(actual.getCotton());
            socksRepository.save(needUpdate);
            log.info("UPDATED SOCKS WITH ID " + needUpdate.getId());
        }
        else {
            log.error("CAN'T FIND SOCKS WITH ID " + actual.getId() + " FOR UPDATE");
            throw new NoSuchElementException();
        }
    }

    public long getCount(Map <String, String> filter)  {
        long result;
        if (filter!= null){
            result = repositorySocks.getCountOfSocksByFilter(filter);
        }
        else result = repositorySocks.getCountOfSocksWithoutFilter();
        log.info("COUNT OF SOCKS WITH FILTER " + filter + " = " + result);
        return  result;
    }

    public Socks getSocksById(long id){
        Optional<Socks> result = socksRepository.findById(Long.toString(id));
        if (result.isPresent())  log.info("FIND SOCKS WITH ID" + id + " " + result.get());
        else{
            log.error("CAN'T FIND SOCKS WITH ID " + id);
            throw  new NoSuchElementException();
        }
        return result.get();
    }

    public void uploadCsvService(MultipartFile file) throws IOException, InvalidCsvException {
        CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()));
        String[] nextLine = new String[0];
        try {
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length != 3) throw new IllegalArgumentException();
                Socks socks = new Socks(Socks.SocksColors.valueOf(nextLine[0]), Integer.parseInt(nextLine[1]), Integer.parseInt(nextLine[2]));
                socksRepository.save(socks);
                log.info("successfully  uploaded from csv " + Arrays.toString(nextLine));
            }
        } catch (Exception e){
            log.error("Failed uploading from csv " + Arrays.toString(nextLine));
            throw  new InvalidCsvException();
        }
    }
}