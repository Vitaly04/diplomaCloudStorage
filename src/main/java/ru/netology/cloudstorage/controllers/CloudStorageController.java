package ru.netology.cloudstorage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudstorage.entity.FileEntity;
import ru.netology.cloudstorage.exception.ErrorDeleteFileException;
import ru.netology.cloudstorage.exception.ErrorGettingFileListException;
import ru.netology.cloudstorage.exception.ErrorInputDataException;
import ru.netology.cloudstorage.exception.ErrorUploadFileException;
import ru.netology.cloudstorage.models.FileEditRequest;
import ru.netology.cloudstorage.service.CloudStorageService;
import ru.netology.cloudstorage.service.JwtUserDetailsService;

import java.io.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "https://jd-homeworks.vercel.app/")
//@CrossOrigin(origins = "http://localhost:8081/")
public class CloudStorageController {

    @Autowired
    CloudStorageService cloudStorageService;
    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @GetMapping(value = "/list")
    public List<FileEntity> getListFiles(@RequestParam("limit") Integer limit, @RequestHeader("auth-token") String token) {
        return cloudStorageService.findUserFiles(limit, token);
    }

    @PostMapping(value = "/file")
    public ResponseEntity<String> saveFile(@RequestParam("filename") String filename, @RequestParam("file") MultipartFile file, @RequestHeader("auth-token") String token) throws IOException {
        return cloudStorageService.saveFile(filename, file, token);
    }

    @GetMapping(value = "/file")
    public ResponseEntity<Resource> getFile(@RequestParam("filename") String filename, @RequestHeader("auth-token") String token) {
        FileEntity fileEntity = cloudStorageService.findById(filename, token);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileEntity.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getFilename() + "\"")
                .body(new ByteArrayResource(fileEntity.getData()));

    }

    @PutMapping(value = "/file")
    public ResponseEntity<String> editFilename(@RequestParam("filename") String filename, @RequestBody FileEditRequest fileEditRequest, @RequestHeader("auth-token") String token) {
        return cloudStorageService.editFilename(filename, fileEditRequest, token);
    }

    @DeleteMapping(value = "/file")
    public ResponseEntity<String> deleteFile(@RequestParam("filename") String filename, @RequestHeader("auth-token") String token) {
        return cloudStorageService.deleteFile(filename, token);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ErrorInputDataException.class)
    public String errorInputDataExceptionHandle(ErrorInputDataException e) {
        return e.getMessage() + e.getId();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ErrorUploadFileException.class)
    public String errorUploadFileExceptionHandle(ErrorUploadFileException e) {
        return e.getMessage() + e.getId();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ErrorGettingFileListException.class)
    public String errorGettingFileListExceptionHandle(ErrorGettingFileListException e) {
        return e.getMessage() + e.getId();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ErrorDeleteFileException.class)
    public String errorDeleteFileExceptionHandle(ErrorDeleteFileException e) {
        return e.getMessage() + e.getId();
    }
}
