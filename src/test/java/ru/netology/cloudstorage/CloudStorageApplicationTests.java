package ru.netology.cloudstorage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.netology.cloudstorage.entity.UserEntity;
import ru.netology.cloudstorage.models.User;
import ru.netology.cloudstorage.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CloudStorageApplication.class)
@AutoConfigureMockMvc
public class CloudStorageApplicationTests {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Test
    public void registerUser() throws Exception {
        User user = new User();
        user.setLogin("111");
        user.setPassword("1234");

        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void findByLogin() {
        UserEntity userEntity = new UserEntity();
        userEntity.setLogin("222");
        userEntity.setPassword("5678");
        userRepository.save(userEntity);
        UserEntity actual = userRepository.findByLogin("222");
        System.out.println(actual);
        Assertions.assertEquals(userEntity, actual);
    }
}
