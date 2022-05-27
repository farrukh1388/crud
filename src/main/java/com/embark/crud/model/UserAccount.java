package com.embark.crud.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserAccount {

    private String id;
    private String email;
    private String username;
    private char[] password;
}
