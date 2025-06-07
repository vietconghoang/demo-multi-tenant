package com.multitenant.service;

import com.multitenant.config.EventContext;
import com.multitenant.entity.UploadResult;
import com.multitenant.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@Service
public class EventService {

    private EventRepository eventRepository;

    public void uploadCsv(MultipartFile file) throws Exception {
        String tenantId = EventContext.getTenantId();
        eventRepository.copyCsvToDatabase(tenantId, file);
    }

    @Async("uploadExecutor")
    public CompletableFuture<UploadResult> uploadCsvAsync(MultipartFile file) {
        try {
            uploadCsv(file);
            return CompletableFuture.completedFuture(
                    new UploadResult(file.getOriginalFilename(), true, "SUCCESS"));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new UploadResult(file.getOriginalFilename(), false, e.getMessage()));
        }
    }

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
}