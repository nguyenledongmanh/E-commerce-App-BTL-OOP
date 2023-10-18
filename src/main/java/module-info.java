module com.group_2.ecommerceapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.json;
    requires static lombok;
    requires java.net.http;


    opens com.group_2.ecommerceapplication to javafx.fxml;
    exports com.group_2.ecommerceapplication;
    exports com.group_2.ecommerceapplication.controller;
    exports com.group_2.ecommerceapplication.model;
    opens com.group_2.ecommerceapplication.controller to javafx.fxml;
    opens com.group_2.ecommerceapplication.model to com.fasterxml.jackson.databind;
}