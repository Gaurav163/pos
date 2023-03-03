package com.increff.pos.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class LoginForm {
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 8)
    private String password;
}
