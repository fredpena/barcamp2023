package dev.fredpena.barcamp.data.common.service;

import dev.fredpena.barcamp.data.common.entity.CommonUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonUserService {

    private final CommonUserRepository repository;


    public CommonUser findUser(String username) {
        return repository.findById(username).orElseThrow();
    }
}
