package ru.netology.cloudstorage.repository;

import org.springframework.data.repository.CrudRepository;
import ru.netology.cloudstorage.entity.FileEntity;

public interface FilesRepository  extends CrudRepository<FileEntity, Long> {
}
