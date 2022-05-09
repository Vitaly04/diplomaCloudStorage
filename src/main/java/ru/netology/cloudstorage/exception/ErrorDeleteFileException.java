package ru.netology.cloudstorage.exception;

import lombok.Data;

@Data
public class ErrorDeleteFileException extends RuntimeException {
    private int id;

    public ErrorDeleteFileException() {
        super("Error delete file");
        id++;
    }
}
