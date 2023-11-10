package dev.fredpena.barcamp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUtils {

    private final ObjectMapper mapper;

    @Data
    public static class TenantPayload {
        @JsonProperty("tenant_id")
        String tenantId;
        String username;
    }

    private final Algorithm algorithm = Algorithm.HMAC256("3A5EB76E-0393-44C1-94AD-F838311FCF21");

    public TenantPayload decode(String token) {

        JWTVerifier verifier = JWT.require(algorithm)
                // specify an specific claim validations
                .withIssuer("auth0")
                // reusable verifier instance
                .build();

        DecodedJWT decodedJWT = verifier.verify(token);

        String jsonPayload = new String(Base64.getDecoder().decode(decodedJWT.getPayload()));

        try {
            return mapper.readValue(jsonPayload, TenantPayload.class);
        } catch (Exception ex) {
            return null;
        }
    }


    public String issueToken(String username) {
        try {
            return JWT.create()
                    .withPayload(Map.of("scope", "common", "username", username))
                    .withIssuer("auth0")
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration / Couldn't convert Claims.
        }

        return null;

    }


    public String issueRefreshToken(String tenant, Long userId, String username) {
        return issueToken("tenant", tenant, userId, username, "refresh");
    }

    public String issueToken(String tenant, Long userId, String username) {
        return issueToken("tenant", tenant, userId, username, "token");
    }

    private String issueToken(String scope, String tenant, Long userId, String username, String type) {
        try {

            return JWT.create()
                    .withPayload(Map.of("scope", scope, "tenant_id", tenant, "user_id", userId, "username", username, "type", type))
                    .withIssuer("auth0")
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration / Couldn't convert Claims.
        }

        return null;
    }

}
