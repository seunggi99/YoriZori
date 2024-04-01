package KNU.YoriZori.service;

import KNU.YoriZori.domain.Fridge;
import KNU.YoriZori.domain.User;
import KNU.YoriZori.exception.UsernameAlreadyExistsException;
import KNU.YoriZori.repository.FridgeRepository;
import KNU.YoriZori.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FridgeRepository fridgeRepository;
    @Transactional
    public Long join(User user) {
        validateDuplicateMember(user); //중복 회원 검증
        Fridge.createFridge(user); // 냉장고 생성 및 회원과 연결
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateMember(User user) {
        userRepository.findByName(user.getName())
                .ifPresent(u -> {
                    throw new UsernameAlreadyExistsException("이미 존재하는 아이디입니다.");
                });
    }

    public User findOne(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found for id: " + userId));
    }

    @Transactional
    public void updateNickname(Long id, String nickname) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found for id: " + id));
        user.setNickname(nickname);
    }

    public User findByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("User not found with name: " + name));
    }


    public Optional<User> authenticate(String name, String password) {
        return userRepository.findByNameAndPassword(name, password);
    }

}
