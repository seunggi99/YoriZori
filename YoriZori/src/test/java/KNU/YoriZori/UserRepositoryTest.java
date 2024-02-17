package KNU.YoriZori;


import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserCrud() {
        // 유저 생성
        User user = new User();
        user.setUsername("john_doe");
        user.setEmail("john@example.com");

        // 저장
        userRepository.save(user);

        // 조회
        User savedUser = userRepository.findById(user.getId()).orElse(null);
        assert savedUser != null;
        assert savedUser.getUsername().equals("john_doe");
        assert savedUser.getEmail().equals("john@example.com");
    }
}
