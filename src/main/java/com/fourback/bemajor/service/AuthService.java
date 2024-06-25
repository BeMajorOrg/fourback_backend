package com.fourback.bemajor.service;

import com.fourback.bemajor.dto.TokenDto;
import com.fourback.bemajor.enums.Role;
import com.fourback.bemajor.exception.InvalidLoginTokenException;
import com.fourback.bemajor.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RedisService redisService;
    private final JWTUtil jwtUtil;

    public TokenDto newToken(String oauth2Id, Role role){
        String newAccess = jwtUtil.createToken("access",oauth2Id, role, 600000L);
        String newRefresh = jwtUtil.createToken("refresh",oauth2Id,role,86400000L);
        TokenDto tokenDto = new TokenDto(newAccess, newRefresh);
        redisService.setRefreshToken(oauth2Id,newRefresh,86400000L);
        return tokenDto;
    }

    public TokenDto checkToken(String refresh){
        if (refresh == null) {
            //response status code
            throw new InvalidLoginTokenException(4, "This is Invalid Token. Try logging in again", HttpStatus.UNAUTHORIZED);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            //response status code
            throw new InvalidLoginTokenException(4, "This is Invalid Token.", HttpStatus.UNAUTHORIZED);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

            //response status code
            throw new InvalidLoginTokenException(4, "This is Invalid Token.", HttpStatus.UNAUTHORIZED);
        }

        String oauth2Id = jwtUtil.getUsername(refresh);
        Role role = jwtUtil.getRole(refresh);

        //make new JWT
        return this.newToken(oauth2Id, role);
    }
}
