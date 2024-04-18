package KNU.YoriZori.service;

import KNU.YoriZori.domain.Fridge;
import KNU.YoriZori.domain.User;
import KNU.YoriZori.dto.ReqRes;
import KNU.YoriZori.exception.UsernameAlreadyExistsException;
import KNU.YoriZori.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.HashMap;
@Service
public class AuthService {

    @Autowired
    private UserRepository ourUserRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public ReqRes signUp(ReqRes registrationRequest){
        ReqRes resp = new ReqRes();
        try {
            // 이름 중복 검사
            ourUserRepo.findByName(registrationRequest.getName())
                    .ifPresent(u -> {
                        throw new UsernameAlreadyExistsException("이미 존재하는 아이디입니다.");
                    });

            User users = new User();
            users.setName(registrationRequest.getName());
            users.setNickname(registrationRequest.getNickname());
            users.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            //users.setRole(registrationRequest.getRole());
            Fridge.createFridge(users);// 냉장고 생성 및 회원과 연결
            User ourUserResult = ourUserRepo.save(users);
            if (ourUserResult != null && ourUserResult.getId()>0) {

                resp.setOurUsers(ourUserResult);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }

        return resp;
    }

    public ReqRes signIn(ReqRes signinRequest){
        ReqRes response = new ReqRes();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getName(),signinRequest.getPassword()));
            var user = ourUserRepo.findByName(signinRequest.getName()).orElseThrow();
            System.out.println("USER IS: "+ user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
        }catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenReqiest){
        ReqRes response = new ReqRes();
        String ourName = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
        User users = ourUserRepo.findByName(ourName).orElseThrow();
        if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
            var jwt = jwtUtils.generateToken(users);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenReqiest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Refreshed Token");
        }
        response.setStatusCode(500);
        return response;
    }
}
