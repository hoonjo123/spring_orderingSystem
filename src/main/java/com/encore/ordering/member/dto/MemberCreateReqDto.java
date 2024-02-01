package com.encore.ordering.member.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class MemberCreateReqDto {

    @NotEmpty(message = "name is essential")
    private String name;

    @NotEmpty(message = "email is essential")
    @Email(message = "email is not valid")
    private String email;

    @NotEmpty(message = "password is essential")
    @Size(min = 4, message = "password should be more than 4 characters")
    private String password;

    private String city;
    private String street;
    private String zipcode;
//    private String role; 사용자가 선택한다는건 말이안됀다.
}
