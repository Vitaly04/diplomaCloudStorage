package ru.netology.cloudstorage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudstorage.entity.FileEntity;
import ru.netology.cloudstorage.entity.UserEntity;
import ru.netology.cloudstorage.exception.ErrorDeleteFileException;
import ru.netology.cloudstorage.exception.ErrorGettingFileListException;
import ru.netology.cloudstorage.exception.ErrorInputDataException;
import ru.netology.cloudstorage.exception.ErrorUploadFileException;
import ru.netology.cloudstorage.models.FileEditRequest;
import ru.netology.cloudstorage.security.JwtTokenUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FilesRepositoryImpl {

    @Autowired
    private FilesRepository filesRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    public ResponseEntity<String> saveFile(@RequestParam("filename") String filename, @RequestParam("file") MultipartFile file, @RequestHeader("auth-token") String token) throws IOException {
        if (isEmpty(filename) || file == null || isEmpty(token)) {
            throw new ErrorInputDataException("Error input data");
        }
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        UserEntity userEntity = userRepository.findByLogin(username);
        filesRepository.save(new FileEntity(filename, userEntity.getId(), file.getContentType(), file.getSize(), file.getBytes()));
        return ResponseEntity.ok().body("Success upload");
    }

    public List findUserFiles(@RequestParam("limit") Integer limit, @RequestHeader("auth-token") String token) {
        if ((limit == null) || isEmpty(token)) {
            throw new ErrorInputDataException("Error input data");
        }
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        UserEntity userEntity = userRepository.findByLogin(username);
        if (userEntity == null) {
            throw new ErrorGettingFileListException("Error getting file list");
        }
        return Arrays.asList(entityManager.createQuery("SELECT f from FileEntity f join UserEntity u on f.userId = u.id where u.login like '" + username + "'")
                .getResultStream().limit(limit).toArray());
    }

    public Optional<FileEntity> getFile(@RequestParam("filename") String filename, @RequestHeader("auth-token") String token) {
        if ((isEmpty(filename)) || isEmpty(token)) {
            throw new ErrorInputDataException("Error input data");
        }
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        Long id = entityManager.createQuery("SELECT f.id from FileEntity f join UserEntity u on f.userId = u.id where f.filename like '" + filename + "'" +
                " and u.login like '" + username + "'", Long.class).getResultStream().findFirst().orElseThrow(ErrorUploadFileException::new);
        return filesRepository.findById(id);
    }

    @Transactional
    public ResponseEntity<String> editFilename(@RequestParam("filename") String filename, @RequestBody FileEditRequest fileEditRequest, @RequestHeader("auth-token") String token) {
        if ((isEmpty(filename)) || isEmpty(token) || isEmpty(fileEditRequest.getFilename())) {
            throw new ErrorInputDataException("Error input data");
        }
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        FileEntity fileEntity = entityManager.createQuery("SELECT f from FileEntity f join UserEntity u on f.userId = u.id where f.filename like '" + filename + "'" +
                " and u.login like '" + username +"'", FileEntity.class).getResultStream().findFirst().orElseThrow(ErrorUploadFileException::new);
        fileEntity.setFilename(fileEditRequest.getFilename());
        entityManager.merge(fileEntity);
        return ResponseEntity.ok().body("Success upload");
    }

    public ResponseEntity<String> deleteFile(@RequestParam("filename") String filename, @RequestHeader("auth-token") String token) {
        if ((isEmpty(filename)) || isEmpty(token)) {
            throw new ErrorInputDataException("Error input data");
        }
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        Long id = entityManager.createQuery("SELECT f.id from FileEntity f join UserEntity u on f.userId = u.id where f.filename like '" + filename + "'" +
                " and u.login like '" + username + "'", Long.class).getResultStream().findFirst().orElseThrow(ErrorDeleteFileException::new);
        filesRepository.deleteById(id);
        return ResponseEntity.ok().body("Success deleted");
    }

    private boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
