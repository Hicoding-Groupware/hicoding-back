package com.hook.hicodingapi.jwt;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUser extends User {

    private final Long memberNo;

    public CustomUser(Long memberNo, UserDetails userDetails) {
        super(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        this.memberNo = memberNo;
    }

    public static CustomUser of(Long memberNo, UserDetails userDetails) {
        return new CustomUser(
                memberNo,
                userDetails
        );
    }
}
