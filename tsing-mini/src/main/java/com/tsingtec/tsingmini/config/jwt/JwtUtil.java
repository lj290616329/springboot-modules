package com.tsingtec.tsingmini.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tsingtec.tsingcore.entity.mini.MaUser;
import com.tsingtec.tsingcore.exception.BusinessException;
import com.tsingtec.tsingcore.exception.code.BaseExceptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("JwtUtil")
@CacheConfig(cacheNames = {"token"})
public class JwtUtil {
 
    @Autowired
    private JwtProperties jwtProperties;

    @Cacheable(key="#maUser.id")
    public String getToken(MaUser maUser) {
        System.out.println("没有缓存!");
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.secretKey);
        Date exp = new Date(System.currentTimeMillis() + jwtProperties.getTokenExpireTime()*60*1000l);
        // 头部信息
        Map<String, Object> header = new HashMap<String, Object>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        String token = JWT.create()
                .withHeader(header)// 设置头部信息 Header
                .withClaim("openid",maUser.getOpenId())
                .withClaim("id",maUser.getId().toString())
                .withExpiresAt(exp)//设置 载荷 签名过期的时间
                .sign(algorithm);//签名 Signature
        return token;
    }

    public boolean verify(String token){
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.secretKey);
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getClaim(String token, String claim) {
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.secretKey);
        try{
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaims().get(claim).asString();
        }catch (IllegalArgumentException e) {
            throw new BusinessException(BaseExceptionType.TOKEN_ERROR,"token认证失败");
        }catch (JWTVerificationException e) {
            throw new BusinessException(BaseExceptionType.TOKEN_ERROR,"token认证失败");
        }
    }
}