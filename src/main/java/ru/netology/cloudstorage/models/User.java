package ru.netology.cloudstorage.models;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements Serializable{
    private String login;
    private String password;
}