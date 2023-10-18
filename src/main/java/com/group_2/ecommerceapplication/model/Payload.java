package com.group_2.ecommerceapplication.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Payload {
    private String sub;
    private Long iat;
    private Long exp;
    private boolean isAdmin;
}