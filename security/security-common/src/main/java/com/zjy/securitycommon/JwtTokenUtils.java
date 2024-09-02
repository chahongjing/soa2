package com.zjy.securitycommon;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.util.Date;
import java.util.HashMap;
import java.util.function.UnaryOperator;

/**
 * Created by echisan on 2018/6/23
 */
public class JwtTokenUtils {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private static final String SECRET = "jwtsecretdemo";
    private static final String ISS = "echisan";

    // 角色的key
    private static final String ROLE_CLAIMS = "rol";

    // 过期时间是3600秒，既是1个小时
    private static final long EXPIRATION = 3600L;

    // 选择了记住我之后的过期时间为7天
    private static final long EXPIRATION_REMEMBER = 604800L;

//    {
//        "alg": "HS256",
//            "typ": "JWT"
//    }
//    iss (issuer)：签发人
//    sub (subject)：主题
//    aud (audience)：受众
//    nbf (Not Before)：生效时间
//    exp (expiration time)：过期时间
//    iat (Issued At)：签发时间
//    jti (JWT ID)：编号

    // 创建token
    public static String createToken(String username, String role, boolean isRememberMe) {
        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
        HashMap<String, Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, role);
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setClaims(map)
                .setIssuer(ISS)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    // 从token中获取用户名
    public static String getUsername(String token) {
        return getTokenBody(token).getSubject();
    }

    // 获取用户角色
    public static String getUserRole(String token) {
        return (String) getTokenBody(token).get(ROLE_CLAIMS);
    }

    // 是否已过期
    public static boolean isExpiration(String token) {
        try {
            return getTokenBody(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    private static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

//    <groupId>com.auth0</groupId>
//    <artifactId>java-jwt</artifactId>
//    @Slf4j
//    public class JwtHelper {
//        public static final long EXPIRE_DEFAULT = 7200000L;
//        public static final String APP_TOKEN_HEADER = "Authorization";
//        public static final String APP_TOKEN_HEADER_BEARER = "Bearer ";
//
//        private JwtHelper() {
//        }
//
//        public static String generateToken(String accessKey, String secretKey) {
//            return generateToken(accessKey, secretKey, EXPIRE_DEFAULT);
//        }
//
//        public static String generateToken(String accessKey, String secretKey, long expire) {
//            Date expiresAt = new Date(System.currentTimeMillis() + expire);
//            Algorithm algorithm = Algorithm.HMAC256(secretKey);
//            return JWT.create().withIssuer(accessKey).withExpiresAt(expiresAt).sign(algorithm);
//        }
//
//        public static String generateAuthorization(String accessKey, String secretKey) {
//            return APP_TOKEN_HEADER_BEARER + generateToken(accessKey, secretKey);
//        }
//
//        public static boolean validateAuthorization(String authorization, UnaryOperator<String> querySecretKey) {
//            if (!Strings.isEmpty(authorization) && authorization.startsWith(APP_TOKEN_HEADER_BEARER)) {
//                String token = authorization.substring(APP_TOKEN_HEADER_BEARER.length());
//                return validate(token, querySecretKey);
//            } else {
//                throw new JwtException("Authorization error");
//            }
//        }
//
//        public static boolean validate(String token, UnaryOperator<String> querySecretKey) {
//            try {
//                DecodedJWT decodedJWT = JWT.decode(token);
//                String secretKey = (String) querySecretKey.apply(decodedJWT.getIssuer());
//                if (Strings.isEmpty(secretKey)) {
//                    throw new JwtException("accessKey error");
//                } else {
//                    Algorithm algorithm = Algorithm.HMAC256(secretKey);
//                    JWTVerifier verifier = JWT.require(algorithm).withIssuer(new String[]{decodedJWT.getIssuer()}).build();
//                    verifier.verify(token);
//
//                    return true;
//                }
//            } catch (Exception var6) {
//                log.error(var6.getClass().getName(), var6);
//                throw new JwtException(var6);
//            }
//        }
//    }
}
