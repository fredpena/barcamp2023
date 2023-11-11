package dev.fredpena.barcamp.controller.common;

import dev.fredpena.barcamp.data.common.entity.CommonUser;
import dev.fredpena.barcamp.data.common.service.CommonUserService;
import dev.fredpena.barcamp.security.JwtUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final JwtUtils jwtUtils;
    private final CommonUserService commonUserService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> doLogin(@RequestBody @Valid @NotNull UserLogin userLogin) {

        CommonUser user = commonUserService.findUser(userLogin.getUsername());
        if (user == null) {
            log.error("Username was not found.");
            return new ResponseEntity<>("No user present with username: " + userLogin.getUsername(), HttpStatus.NOT_FOUND);
        }

        if (!passwordEncoder.matches(userLogin.getPassword(), user.getPassword())) {
            log.error("Username's password didn't match.");
            return new ResponseEntity<>("Username's password didn't match.: " + userLogin.getUsername(), HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(jwtUtils.issueToken(userLogin.getUsername()));
    }
}