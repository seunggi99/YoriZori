package KNU.YoriZori.service;

import KNU.YoriZori.domain.Fridge;
import KNU.YoriZori.domain.User;
import KNU.YoriZori.repository.FridgeRepository;
import KNU.YoriZori.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired FridgeService fridgeService;
    @Autowired FridgeRepository fridgeRepository;
    @Autowired EntityManager em;


    @Test
    public void 회원가입() throws Exception {
        //given
        User user = new User();
        user.setName("kim777");
        user.setNickname("소프트");
        user.setPassword("1234");

        //when
        Long savedId = userService.join(user);
        //then
        User foundUser = userRepository.findById(savedId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + savedId));
        assertEquals(user, foundUser);
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        User user1 = new User();
        user1.setName("kimkim");

        User user2 = new User();
        user2.setName("kimkim");

        //when
        userService.join(user1);
        userService.join(user2); //예외 발생

        //then
        fail("예외발생");
    }
}