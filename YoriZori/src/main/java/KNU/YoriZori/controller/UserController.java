package KNU.YoriZori.controller;

import KNU.YoriZori.domain.User;
import KNU.YoriZori.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원 가입
    @PostMapping("/users")
    public CreateUserResponse saveUser(@RequestBody @Valid CreateUserRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());

        Long id = userService.join(user);
        return new CreateUserResponse(id);
    }

    // 회원 닉네임 변경
    @PutMapping("users/{id}")
    public UpdateUserNicknameResponse updateUserNickname(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateUserNicknameRequest request){
        userService.updateNickname(id, request.getNickname());
        User findUser = userService.findOne(id);
        return new UpdateUserNicknameResponse(findUser.getId(), findUser.getNickname());
    }

    @GetMapping("/login")
    public ResponseEntity<UserResponseDTO> getUserByName(@RequestParam String name) {
        User user = userService.findByName(name);
        UserResponseDTO responseDTO = new UserResponseDTO();

        responseDTO.setId(user.getId());
        responseDTO.setPassword(user.getPassword()); // 보안상 실제로는 비밀번호를 반환하지 않음
        responseDTO.setNickname(user.getNickname());

        return ResponseEntity.ok(responseDTO);
    }

    @Data
    static class UpdateUserNicknameRequest{
        private String nickname;
    }
    @Data
    @AllArgsConstructor
    static class UpdateUserNicknameResponse{
        private Long id;
        private String nickname;
    }

    @Data
    static class CreateUserRequest {
        private String name;
        private String password;
        private String nickname;
    }

    @Data
    static class CreateUserResponse {
        private Long id;
        public CreateUserResponse(Long id) {
            this.id = id;
        }
    }
    @Data
    public class UserResponseDTO {
        private Long id;
        private String password;
        private String nickname;
    }
}
