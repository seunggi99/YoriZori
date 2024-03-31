package KNU.YoriZori.controller;

import KNU.YoriZori.domain.User;
import KNU.YoriZori.service.UserService;
import jakarta.validation.Valid;
import lombok.*;
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
        return new CreateUserResponse(id); //test
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


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        return userService.authenticate(loginRequest.getName(), loginRequest.getPassword())
                .map(user -> ResponseEntity.ok(new ApiResponse(true, user.getId(), user.getNickname())))
                .orElse(ResponseEntity.ok(new ApiResponse(false, "아이디 혹은 비밀번호를 확인해주십시오.")));
    }

    @Data
    public class ApiResponse {
        private boolean success;
        private String message;
        private Long userId;
        private String nickname;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public ApiResponse(boolean success, Long userId, String nickname) {
            this.success = success;
            this.userId = userId;
            this.nickname = nickname;
        }

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

    @Data
    @NoArgsConstructor
    public static class LoginRequest {
        private String name;
        private String password;
    }

    @Data
    @AllArgsConstructor
    public class LoginResponse {
        private Long id;
        private String nickname;
    }

}
