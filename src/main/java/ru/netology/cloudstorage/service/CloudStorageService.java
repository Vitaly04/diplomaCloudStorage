package ru.netology.cloudstorage.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudstorage.entity.FileEntity;
import ru.netology.cloudstorage.exception.ErrorUploadFileException;
import ru.netology.cloudstorage.models.FileEditRequest;
import ru.netology.cloudstorage.repository.FilesRepositoryImpl;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class CloudStorageService {

    private FilesRepositoryImpl filesRepositoryImpl;

    public List<FileEntity> findUserFiles(@RequestParam("limit") Integer limit, @RequestHeader("auth-token") String token) {
        return filesRepositoryImpl.findUserFiles(limit, token);
    }
    public ResponseEntity<String> saveFile(@RequestParam("filename") String filename, @RequestParam("file") MultipartFile file, @RequestHeader("auth-token") String token) throws IOException {
        return filesRepositoryImpl.saveFile(filename, file, token);
    }
    public ResponseEntity<String> editFilename(@RequestParam("filename") String filename, @RequestBody FileEditRequest fileEditRequest, @RequestHeader("auth-token") String token) {
        return filesRepositoryImpl.editFilename(filename, fileEditRequest, token);
    }

    public FileEntity findById(@RequestParam("filename") String filename, @RequestHeader("auth-token") String token) {
        return filesRepositoryImpl.getFile(filename, token).orElseThrow(ErrorUploadFileException::new);
    }

    public ResponseEntity<String> deleteFile(@RequestParam("filename") String filename, @RequestHeader("auth-token") String token) {
        return filesRepositoryImpl.deleteFile(filename, token);
    }
}
