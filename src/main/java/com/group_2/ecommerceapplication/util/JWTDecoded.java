package com.group_2.ecommerceapplication.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group_2.ecommerceapplication.model.Payload;

import java.util.Base64;

public class JWTDecoded {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static boolean isAdmin(String token) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = token.split("\\.");
        String payload_body = new String(decoder.decode(chunks[1]));
        try {
            Payload payload = mapper.readValue(payload_body, Payload.class);
            return payload.isAdmin();
        } catch (Exception e) {
            System.out.println("Some exception");
        }
        return false;
    }
}
