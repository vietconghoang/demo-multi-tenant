package com.multitenant.controller;

import com.multitenant.entity.UploadResult;
import com.multitenant.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class EventController {
     private EventService eventService;

    @PostMapping("/events")
    public ResponseEntity<List<UploadResult>> uploadFiles(
            @RequestParam("files") MultipartFile[] files) {

        List<CompletableFuture<UploadResult>> futures = Arrays.stream(files)
                .map(eventService::uploadCsvAsync)
                .toList();

        List<UploadResult> results = futures.stream()
                .map(CompletableFuture::join)
                .toList();

        return ResponseEntity.ok(results);
    }

    @Autowired
    public EventController( EventService aEventService) {
        this.eventService = aEventService;
    }
}
