package com.group_2.ecommerceapplication.controller;

import com.group_2.ecommerceapplication.MainApplication;
import com.group_2.ecommerceapplication.model.Product;
import com.group_2.ecommerceapplication.util.AppConstant;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardController {

    @FXML
    private ImageView product_img;

    @FXML
    private Label product_name;

    @FXML
    private Label product_price;

    @FXML
    private Label product_sold;

    public void setData(Product product) {
        Image image = new Image(AppConstant.API_LINK.get("images") + "/" + product.getImgSrc());
        product_img.setImage(image);
        product_name.setText(product.getName());
        product_price.setText(String.valueOf(product.getPrice()));
        product_sold.setText(String.valueOf(product.getSold()));
    }
}
