package com.group_2.ecommerceapplication.controller;

import com.group_2.ecommerceapplication.MainApplication;
import com.group_2.ecommerceapplication.model.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    @FXML
    private AnchorPane product_layout;

    private List<Product> products;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        products = new ArrayList<>(getList());
    }

    private List<Product> getList() {
        List<Product> ls = new ArrayList<>();
        Product product = new Product();
        product.setName("ABCD");
        product.setImageSrc("img/op_dien_thoai1.jpeg");
        product.setPrice(5000);
        product.setSold(100);
        ls.add(product);

        Product product2 = new Product();
        product2.setName("ABCDE");
        product2.setImageSrc("img/op_dien_thoai1.jpeg");
        product2.setPrice(5000);
        product2.setSold(100);
        ls.add(product2);

        return ls;
    }
}
