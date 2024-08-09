package com.codepulse.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class CustomerUserDetails extends org.springframework.security.core.userdetails.User {

    private String firstName;
    private String lastName;
    private String gender;
    private String phoneNumber;

    public CustomerUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
                               String gender, String firstName, String lastName, String phoneNumber) {
        super(username, password, authorities);
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
}


