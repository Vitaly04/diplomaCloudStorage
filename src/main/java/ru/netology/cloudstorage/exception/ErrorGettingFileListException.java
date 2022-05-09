package ru.netology.cloudstorage.exception;

import lombok.Data;

@Data
public class ErrorGettingFileListException extends RuntimeException {
    private int id;

    public ErrorGettingFileListException(String message) {
        super(message);
        id++;
    }
}
