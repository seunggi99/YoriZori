package KNU.YoriZori.service;

import KNU.YoriZori.domain.User;
import KNU.YoriZori.repository.FridgeRepository;
import KNU.YoriZori.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FridgeRepository fridgeRepository;
    @Transactional
    public Long join(User user) {
        validateDuplicateMember(user); //중복 회원 검증
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateMember(User user) {
        List<User> findMembers = userRepository.findByName(user.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    public User findOne(Long userId) {
        return userRepository.findOne(userId);
    }
}
