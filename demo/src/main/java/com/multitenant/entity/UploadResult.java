package com.multitenant.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadResult {
    private String fileName;
    private boolean success;
    private String message;

    public UploadResult(String fileName, boolean success, String message) {
        this.fileName = fileName;
        this.success = success;
        this.message = message;
    }

}
