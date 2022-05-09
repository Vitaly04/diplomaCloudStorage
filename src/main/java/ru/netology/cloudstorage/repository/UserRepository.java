package ru.netology.cloudstorage.repository;

import org.springframework.data.repository.CrudRepository;
import ru.netology.cloudstorage.entity.UserEntity;


public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    UserEntity findByLogin(String login);
}
