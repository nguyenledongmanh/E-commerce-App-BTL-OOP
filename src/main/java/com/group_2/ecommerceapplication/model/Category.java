package com.group_2.ecommerceapplication.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private int id;
    private String name;
    private String desc;
    private List<Product> products;
    @Override
    public String toString() {
        return name;
    }
}
