package dev.fredpena.barcamp.controller.common;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLogin {

    @NotNull
    private String username;
    @NotNull
    private String password;
}
