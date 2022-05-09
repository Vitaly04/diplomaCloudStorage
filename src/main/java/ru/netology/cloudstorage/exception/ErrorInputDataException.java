package ru.netology.cloudstorage.exception;

import lombok.Data;

@Data
public class ErrorInputDataException extends RuntimeException {
    private int id;

    public ErrorInputDataException(String message) {
        super(message);
        id++;
    }
}
