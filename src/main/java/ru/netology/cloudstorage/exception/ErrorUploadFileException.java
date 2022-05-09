package ru.netology.cloudstorage.exception;

import lombok.Data;

@Data
public class ErrorUploadFileException extends RuntimeException {
    private int id;

    public ErrorUploadFileException() {
        super("Error upload file");
        id++;
    }
}
