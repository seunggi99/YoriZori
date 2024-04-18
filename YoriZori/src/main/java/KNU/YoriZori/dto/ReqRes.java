package KNU.YoriZori.dto;

import KNU.YoriZori.domain.Fridge;
import KNU.YoriZori.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqRes {
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;

    private String name;
    private String nickname;
    private String password;
   // private List<Fridge> products;
    private User ourUsers;
}
