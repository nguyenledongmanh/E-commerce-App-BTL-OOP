package com.group_2.ecommerceapplication.controller;

import com.group_2.ecommerceapplication.model.Category;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CategoryCardController {

    @FXML
    private Label category_name;

    @FXML
    private Label number_of_products;

    public void setData(Category category) {
        category_name.setText(category.getName());
        number_of_products.setText(String.valueOf(category.getProducts()
                                                          .size()));
    }
}
