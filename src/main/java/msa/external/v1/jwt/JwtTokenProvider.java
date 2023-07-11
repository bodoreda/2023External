package msa.external.v1.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import msa.external.v1.dto.UserInfo;
import msa.external.v1.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * packageName : msa.external.v1.jwt
 * fileName : JwtTokenProvider
 * author : BH
 * date : 2023-07-04
 * description :
 * ================================================
 * DATE                AUTHOR              NOTE
 * ================================================
 * 2023-07-04       JwtTokenProvider       최초 생성
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class JwtTokenProvider {
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisUtils redisUtils;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.token.accessExpTime}")
    private long accessExpirationTime;

    @Value("${spring.jwt.token.refreshExpTime}")
    private long refreshExpirationTime;


    /**
     * Access 토큰 생성
     */
    public String createAccessToken(String cuid, List<String> roles){
        Claims claims = Jwts.claims().setSubject(cuid);
        claims.put("roles", roles);
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessExpirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Refresh 토큰 생성
     */
    public String createRefreshToken(String cuid, UserInfo userInfo, List<String> roles){
        Claims claims = Jwts.claims().setSubject(cuid);
        claims.put("roles", roles);
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshExpirationTime);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        // Redis에 refreshToken 저장
        redisUtils.setData("refreshToken", refreshToken, refreshExpirationTime);
        // Redis에 UserInfo 저장
        redisUtils.setUserInfo("userInfo", userInfo, refreshExpirationTime);

        return refreshToken;
    }

    /**
     * http 헤더로부터 bearer 토큰을 가져옴.
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Access 토큰을 검증
     */
    public boolean validateToken(String accessToken) {
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken);
            log.info("토큰 검증 성공({})", getClass());
            return true;
        } catch(ExpiredJwtException e) {
            log.error("만료된 토큰");
            return false;
        } catch(JwtException e) {
            log.error("유효하지 않은 토큰");
            return false;
        }
    }
}
