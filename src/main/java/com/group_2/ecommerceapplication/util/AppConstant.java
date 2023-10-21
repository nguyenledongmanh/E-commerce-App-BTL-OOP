package com.group_2.ecommerceapplication.util;

import com.group_2.ecommerceapplication.MainApplication;
import javafx.scene.image.Image;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AppConstant {
    public final static String BASE_URL = "http://localhost:8080/api";
    public static String username = "";
    public static String token = "";
    public static Map<String, String> API_LINK = new HashMap<>(getAPI_LINK());

    public static Image DEFAULT_IMAGE = new Image(String.valueOf(new File(
            String.valueOf(MainApplication.class.getResource("img/user.png")))));

    public static void clear() {
        username = "";
        token = "";
    }

    private static Map<String, String> getAPI_LINK() {
        Map<String, String> link = new HashMap<>();
        link.put("products", BASE_URL + "/products");
        link.put("categories", BASE_URL + "/categories");
        link.put("images", BASE_URL + "/images");
        link.put("login", BASE_URL + "/auth/login");
        link.put("register", BASE_URL + "/auth/register");
        return link;
    }
}
