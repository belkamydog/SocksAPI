package com.api.socks.controller;

import com.api.socks.exception.InvalidCsvException;
import com.api.socks.exceptions.LackOfSocksException;
import com.api.socks.models.Socks;
import com.api.socks.service.RepositoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Tag(name = "Socks repository service", description = "socks API")
@ControllerAdvice
@RestController("/")
public class SocksController {
    @Autowired
    private RepositoryService service;

    @Tag(name = "add socks", description = "add new socks to repository or up count if it already exist")
    @PostMapping("api/socks/income")
    public ResponseEntity<String> addSocks(@RequestBody Socks socks){
        service.addSocks(socks);
        return ResponseEntity.status(201).body("successfully added");
    }

    @Tag(name = "extract socks", description = "extract socks from the repository")
    @PostMapping("api/socks/outcome")
    public ResponseEntity<String> removeSocks(@RequestBody Socks socks) throws LackOfSocksException {
        service.removeSocks(socks);
        return ResponseEntity.ok("successfully extract");
    }

    @Tag(name = "update", description = "update socks fields by id")
    @PutMapping("api/socks/{id}")
    public ResponseEntity<String> update(@PathVariable long id, @RequestBody Socks socks){
        service.update(id, socks);
        return ResponseEntity.ok().body("successfully updated");
    }

    @Tag(name = "get count of socks", description = "get count of socks by filter filter example:(color=red,white&cotton=lt:100+and+gt:0+and+eq:5)")
    @GetMapping("api/socks")
    public ResponseEntity<Long> getCountOfSocks(@RequestParam (required = false) Map <String, String> filter) {
        Long result = service.getCount(filter);
        return ResponseEntity.ok().body(result);
    }

    @Tag(name = "get by id", description = "get socks by id")
    @GetMapping("api/socks/{id}")
    public ResponseEntity<Socks> getSocksById(@PathVariable long id){
        return ResponseEntity.ok().body(service.getSocksById(id));
    }

    @Tag(name = "upload csv", description = "get socks from csv file")
    @PostMapping("api/socks/batch")
    public ResponseEntity<String> uploadFromCsv(@RequestParam ("file")MultipartFile file) throws IOException, InvalidCsvException {
        service.uploadCsvService(file);
        return ResponseEntity.ok().body("successfully loaded");
    }
}
