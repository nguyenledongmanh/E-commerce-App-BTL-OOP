package com.group_2.ecommerceapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group_2.ecommerceapplication.MainApplication;
import com.group_2.ecommerceapplication.model.JWTResponse;
import com.group_2.ecommerceapplication.util.AppConstant;
import com.group_2.ecommerceapplication.util.JWTDecoded;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class LoginController
        implements Initializable {

    private final TranslateTransition su_slide = new TranslateTransition(Duration.seconds(1));
    private final TranslateTransition si_slide = new TranslateTransition(Duration.seconds(1));
    private final ObjectMapper mapper = new ObjectMapper();
    private HttpClient httpClient;
    private Alert alert;


    @FXML
    private Label intro_form1;

    @FXML
    private Label intro_form2;

    @FXML
    private Button login_btn;

    @FXML
    private AnchorPane parent;

    @FXML
    private Button register_btn;

    @FXML
    private Hyperlink register_link;

    @FXML
    private AnchorPane si_form;

    @FXML
    private PasswordField si_password;

    @FXML
    private TextField si_username;

    @FXML
    private Hyperlink sign_in_link;

    @FXML
    private TextField su_email;

    @FXML
    private AnchorPane su_form;

    @FXML
    private PasswordField su_password;

    @FXML
    private TextField su_username;


    // Change to register form
    @FXML
    void register_link(ActionEvent event) {
        si_slide.setToX(400);
        si_slide.play();
        su_form.setVisible(true);
        su_slide.setNode(su_form);
        su_slide.setToX(0);
        su_slide.play();
    }

    // Change to login form
    @FXML
    void sign_in_link(ActionEvent event) {
        su_slide.setToX(400);
        su_slide.play();
        si_form.setVisible(true);
        si_slide.setToX(0);
        si_slide.play();
    }

    // Submit register form
    @FXML
    void register(ActionEvent event) {
        Map<String, String> registerInfo = new HashMap<>();
        registerInfo.put("username", su_username.getText());
        registerInfo.put("password", su_password.getText());
        registerInfo.put("email", su_email.getText());

        try {
            String body = mapper.writer()
                                .writeValueAsString(registerInfo);
            HttpRequest request = HttpRequest.newBuilder()
                                             .uri(URI.create(AppConstant.API_LINK.get("register")))
                                             .header("Content-type", "application/json")
                                             .POST(HttpRequest.BodyPublishers.ofString(body))
                                             .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            if (status >= 200 && status <= 299) {
                showAlert(Alert.AlertType.INFORMATION, "Register Successfully");
                su_username.setText("");
                su_password.setText("");
                su_email.setText("");
            } else {
                Object response_obj = mapper.readValue(response.body(), Object.class);
                Map<?, ?> res_map = (Map<?, ?>) response_obj;
                StringBuilder content = new StringBuilder();
                res_map.forEach((key, value) -> content.append(key.toString())
                                                       .append(": ")
                                                       .append(value.toString())
                                                       .append("\n"));
                showAlert(Alert.AlertType.INFORMATION, String.valueOf(content));
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "INTERNAL_SERVER_ERROR");
        }
    }

    @FXML
    void login(ActionEvent event) {
        Map<String, String> loginInfo = new HashMap<>();
        loginInfo.put("usernameOrEmail", si_username.getText());
        loginInfo.put("password", si_password.getText());

        try {
            String body = mapper.writer()
                                .writeValueAsString(loginInfo);

            HttpRequest request = HttpRequest.newBuilder()
                                             .uri(URI.create(AppConstant.API_LINK.get("login")))
                                             .header("Content-type", "application/json")
                                             .POST(HttpRequest.BodyPublishers.ofString(body))
                                             .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode <= 299 & statusCode >= 200) {
                String jsonResponse = response.body();
                JWTResponse jwtResponse = mapper.readValue(jsonResponse, JWTResponse.class);
                String token = jwtResponse.getAccessToken();
                if (JWTDecoded.isAdmin(token)) {
                    AppConstant.username = loginInfo.get("usernameOrEmail");
                    AppConstant.token = jwtResponse.getTokenType() + " " + jwtResponse.getAccessToken();
                    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("admin-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();

                    stage.setTitle("ADMIN MANAGEMENT");

                    stage.setScene(scene);
                    stage.setResizable(false);

                    stage.show();
                } else {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("user-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();

                    stage.setTitle("USER WINDOW");

                    stage.setScene(scene);
                    stage.setResizable(false);

                    stage.show();
                }
                si_form.getScene()
                       .getWindow()
                       .hide();
            } else {
                showAlert(Alert.AlertType.ERROR, "Incorrect username or password");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "INTERNAL_SERVER_ERROR");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TranslateTransition intro_slider = new TranslateTransition();
        TranslateTransition intro_slide2 = new TranslateTransition();
        TranslateTransition parent_slide = new TranslateTransition();

        intro_slider.setNode(intro_form1);
        intro_slider.setToX(33);
        intro_slider.setDuration(Duration.seconds(1));
        intro_slider.play();

        intro_slide2.setNode(intro_form2);
        intro_slide2.setToY(230);
        intro_slide2.setDuration(Duration.seconds(1));
        intro_slide2.play();

        parent_slide.setNode(parent);
        parent_slide.setToX(0);
        parent_slide.setDuration(Duration.seconds(1));
        parent_slide.play();

        si_slide.setNode(si_form);
        si_slide.setToX(0);
        si_slide.play();

        httpClient = HttpClient.newHttpClient();
        alert = new Alert(Alert.AlertType.NONE);
    }

    private void showAlert(Alert.AlertType type, String content) {
        alert.setAlertType(type);
        alert.setContentText(content);
        alert.show();
    }
}
