package ru.netology.cloudstorage.exception;

import lombok.Data;

@Data
public class ErrorBadCredentialException extends RuntimeException {
    private int id;

    public ErrorBadCredentialException(String msg) {
        super(msg);
        id++;
    }
}
