package com.group_2.ecommerceapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group_2.ecommerceapplication.MainApplication;
import com.group_2.ecommerceapplication.model.Category;
import com.group_2.ecommerceapplication.model.Product;
import com.group_2.ecommerceapplication.util.AppConstant;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AdminController
        implements Initializable {

    HttpClient client;
    ObjectMapper mapper;
    Alert alert;
    List<Category> categories;
    List<Product> products;
    URI fileChosen;

    @FXML
    private ComboBox<Category> category_options;
    @FXML
    private Button add_category_btn;
    @FXML
    private AnchorPane add_category_layout;
    @FXML
    private Button add_product_btn;
    @FXML
    private AnchorPane add_product_layout;
    @FXML
    private AnchorPane categories_layout;
    @FXML
    private TextArea cat_desc;
    @FXML
    private TextField cat_name;
    @FXML
    private Button cat_submit_btn;
    @FXML
    private Button categories_btn;
    @FXML
    private Button choose_file_btn;
    @FXML
    private Label num_of_categories;
    @FXML
    private Label num_of_customers;
    @FXML
    private Label num_of_new_orders;
    @FXML
    private Label num_of_products;
    @FXML
    private Button orders_confirmation_btn;
    @FXML
    private TextArea pro_desc;
    @FXML
    private ImageView pro_img;
    @FXML
    private TextField pro_name;
    @FXML
    private TextField pro_price;
    @FXML
    private TextField pro_quantity;
    @FXML
    private TextField pro_sku;
    @FXML
    private Button pro_submit_btn;
    @FXML
    private GridPane product_container;
    @FXML
    private GridPane category_container;
    @FXML
    private AnchorPane product_layout;
    @FXML
    private Button products_btn;
    @FXML
    private Label username_field;

    @FXML
    void choose_file(ActionEvent event) throws
            URISyntaxException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(Objects.requireNonNull(MainApplication.class.getResource("img/"))
                                                        .toURI()));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            Image image = new Image(file.toURI()
                                        .toString());
            fileChosen = file.toURI();
            pro_img.setImage(image);
        }
    }

    @FXML
    void cat_submit(ActionEvent event) {
        Map<String, String> cat_info = new HashMap<>();
        cat_info.put("name", cat_name.getText()
                                     .trim());
        cat_info.put("desc", cat_desc.getText()
                                     .trim());
        try {
            String body = mapper.writer()
                                .writeValueAsString(cat_info);
            HttpRequest request = HttpRequest.newBuilder()
                                             .uri(URI.create(AppConstant.API_LINK.get("categories")))
                                             .header("Content-type", "application/json")
                                             .header("Authorization", AppConstant.token)
                                             .POST(HttpRequest.BodyPublishers.ofString(body))
                                             .build();
            CompletableFuture<HttpResponse<String>> future = client.sendAsync(request,
                                                                              HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> httpResponse = future.get();
            int statusCode = httpResponse.statusCode();
            if (statusCode >= 200 && statusCode <= 299) {
                Object cat = mapper.readValue(httpResponse.body(), Object.class);
                Category category = new Category();
                category.setId((int) ((Map<?, ?>) cat).get("id"));
                category.setName(((Map<?, ?>) cat).get("name")
                                                  .toString());
                category.setDesc(((Map<?, ?>) cat).get("desc")
                                                  .toString());
                categories.add(category);
                clearCatInfo();
                num_of_categories.setText(String.valueOf(categories.size()));
            } else
                throw new Exception("INTERNAL_SERVER_ERROR");

        } catch (Exception e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("INTERNAL_SERVER_ERROR");
            alert.show();
        }
    }

    @FXML
    void pro_submit(ActionEvent e) {
        Map<String, Object> pro_info = new HashMap<>();
        pro_info.put("name", pro_name.getText()
                                     .trim());
        pro_info.put("desc", pro_desc.getText()
                                     .trim());
        pro_info.put("sku", pro_sku.getText()
                                   .trim());
        pro_info.put("price", Double.parseDouble(pro_price.getText()
                                                          .trim()));
        pro_info.put("quantity", Integer.parseInt(pro_quantity.getText()
                                                              .trim()));
        pro_info.put("categoryId", category_options.getSelectionModel()
                                                   .getSelectedItem()
                                                   .getId());
        try {
            String body = mapper.writer()
                                .writeValueAsString(pro_info);
            HttpRequest post_Request = HttpRequest.newBuilder()
                                                  .uri(URI.create(AppConstant.API_LINK.get("products")))
                                                  .header("Content-type", "application/json")
                                                  .header("Authorization", AppConstant.token)
                                                  .POST(HttpRequest.BodyPublishers.ofString(body))
                                                  .build();

            HttpResponse<String> response = client.send(post_Request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode >= 200 && statusCode <= 299) {
                String proJson = response.body();
                System.out.println(proJson);
                Product product = new Product();
                Object proObj = mapper.readValue(proJson, Object.class);
                int productId = (int) ((Map<?, ?>) proObj).get("id");

                File file = new File(fileChosen);
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.addPart("image", new FileBody(file));
                builder.addTextBody("name", file.getName());
                builder.addTextBody("productId", String.valueOf(productId));
                HttpPost img_request = new HttpPost(AppConstant.API_LINK.get("images"));
                img_request.setEntity(builder.build());
                img_request.setHeader("Authorization", AppConstant.token);

                org.apache.http.client.HttpClient httpClient = HttpClientBuilder.create()
                                                                                .build();
                org.apache.http.HttpResponse img_response = httpClient.execute(img_request);
                int status = img_response.getStatusLine()
                                         .getStatusCode();
                System.out.println(status);
                if (status <= 299 && status >= 200) {
                    product.setId(productId);
                    product.setName((String) ((Map<?, ?>) proObj).get("name"));
                    product.setImageSrc(file.getName());
                    product.setSold((int) ((Map<?, ?>) proObj).get("quantity"));
                    product.setPrice((double) ((Map<?, ?>) proObj).get("price"));
                    products.add(product);

                    clearProductInfo();

                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setContentText("Product is added successfully..!");
                    alert.show();

                    num_of_products.setText(String.valueOf(products.size()));

                } else {
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setContentText("Image exist by this name");
                    alert.show();
                }
            } else {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("INTERNAL_SERVER_ERROR");
                alert.show();
            }
        } catch (Exception ex) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("INTERNAL_SERVER_ERROR");
            alert.show();
        }

    }

    @FXML
    void switchForm(ActionEvent event) throws
            IOException,
            InterruptedException {
        init_update();
        if (event.getSource() == products_btn) {
            product_layout.setVisible(true);
            categories_layout.setVisible(false);
            add_category_layout.setVisible(false);
            add_product_layout.setVisible(false);
            update_products();
        } else if (event.getSource() == categories_btn) {
            product_layout.setVisible(false);
            categories_layout.setVisible(true);
            add_category_layout.setVisible(false);
            add_product_layout.setVisible(false);
            update_categories();
        } else if (event.getSource() == add_category_btn) {
            product_layout.setVisible(false);
            categories_layout.setVisible(false);
            add_category_layout.setVisible(true);
            add_product_layout.setVisible(false);
        } else if (event.getSource() == add_product_btn) {
            product_layout.setVisible(false);
            categories_layout.setVisible(false);
            add_category_layout.setVisible(false);
            add_product_layout.setVisible(true);
            category_options.setItems(FXCollections.observableArrayList(categories));
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = HttpClient.newHttpClient();
        mapper = new ObjectMapper();
        alert = new Alert(Alert.AlertType.NONE);

        categories = new ArrayList<>();
        products = new ArrayList<>();
        username_field.setText(AppConstant.username);

        init_update();
        update_products();

        category_options.setItems(FXCollections.observableArrayList(categories));
    }

    private void init_update() {
        try {
            List<URI> targets = Arrays.asList(
                    new URI(AppConstant.API_LINK.get("categories")),
                    new URI(AppConstant.API_LINK.get("products"))
            );

            List<CompletableFuture<String>> futures = targets.stream()
                                                             .map(target -> client
                                                                     .sendAsync(
                                                                             HttpRequest.newBuilder(target)
                                                                                        .GET()
                                                                                        .build(),
                                                                             HttpResponse.BodyHandlers.ofString())
                                                                     .thenApply(HttpResponse::body))
                                                             .toList();

            Object categories = mapper.readValue(futures.get(0)
                                                        .get(), Object.class);
            Object products = mapper.readValue(futures.get(1)
                                                      .get(), Object.class);

            Map<?, ?> categoriesJson = (Map<?, ?>) categories;
            Map<?, ?> productsJson = (Map<?, ?>) products;


            num_of_categories.setText(String.valueOf(categoriesJson.get("totalElements")
                                                                   .toString()));
            num_of_products.setText(productsJson.get("totalElements")
                                                .toString());

            List<?> catContent = (List<?>) categoriesJson.get("content");
            init_update_cat(catContent);

            List<?> proContent = (List<?>) productsJson.get("content");
            init_update_pro(proContent);
        } catch (URISyntaxException | ExecutionException | InterruptedException | IOException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("INTERNAL_SERVER_ERROR");
            alert.show();
        }
    }


    private void update_products() {
        int column = 0;
        int row = 1;
        for (Product product : products) {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("card-view.fxml"));
            try {
                VBox cardBox = fxmlLoader.load();
                CardController cardController = fxmlLoader.getController();
                cardController.setData(product);
                if (column == 6) {
                    column = 0;
                    row += 1;
                }
                product_container.add(cardBox, column++, row);
                GridPane.setMargin(cardBox, new Insets(10));
                cardBox.setOnMouseClicked(mouseEvent -> {
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void init_update_cat(List<?> catContent) {
        this.categories.clear();
        for (Object c : catContent) {
            Map<?, ?> m_Cat = (Map<?, ?>) c;
            Category category = new Category();
            category.setId((int) m_Cat.get("id"));
            category.setName((String) m_Cat.get("name"));
            category.setDesc((String) m_Cat.get("desc"));
            this.categories.add(category);
        }
    }

    private void init_update_pro(List<?> proContent) throws
            IOException,
            InterruptedException {
        this.products.clear();
        for (Object p : proContent) {
            Map<?, ?> p_val = (Map<?, ?>) p;
            Product product = setProductByMap(p_val);
            this.products.add(product);
        }
    }

    private void update_categories() throws
            IOException,
            InterruptedException {

        int column = 0;
        int row = 1;

        for (Category c : categories) {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("category-card-view.fxml"));
            HBox cardBox = fxmlLoader.load();
            if (column == 6) {
                column = 0;
                row += 1;
            }
            CategoryCardController cardController = fxmlLoader.getController();
            HttpRequest pro_request = HttpRequest.newBuilder()
                                                 .uri(URI.create(AppConstant.API_LINK.get(
                                                         "products") + "/categories/" + c.getId()))
                                                 .header("Content-type", "application/json")
                                                 .GET()
                                                 .build();

            HttpResponse<String> pro_response = client.send(pro_request, HttpResponse.BodyHandlers.ofString());
            if (pro_response.statusCode() < 200 || pro_response.statusCode() > 299) {
                throw new InterruptedException("INTERNAL SERVER ERROR");
            }

            Object o = mapper.readValue(pro_response.body(), Object.class);
            List<?> products_of_cat = (List<?>) o;
            List<Product> productList = new ArrayList<>();
            for (Object p: products_of_cat) {
                Map<?, ?> p_val = (Map<?, ?>) p;
                Product product = setProductByMap(p_val);
                productList.add(product);
            }
            c.setProducts(productList);
            cardController.setData(c);
            category_container.add(cardBox, column++, row);
            GridPane.setMargin(cardBox, new Insets(10));
        }
    }

    private void clearProductInfo() {
        pro_name.setText("");
        pro_desc.setText("");
        pro_sku.setText("");
        pro_quantity.setText("");
        pro_price.setText("");
        category_options.getSelectionModel()
                        .selectFirst();
        pro_img.setImage(AppConstant.DEFAULT_IMAGE);
    }

    private void clearCatInfo() {
        cat_name.setText("");
        cat_desc.setText("");
    }

    private HttpResponse<String> getImgByProductId(int productId) throws
            IOException,
            InterruptedException {
        String pro_img_link = AppConstant.API_LINK.get("images") + "/products/" + productId;

        HttpRequest img_req = HttpRequest.newBuilder()
                                         .uri(URI.create(pro_img_link))
                                         .header("Content-Type", "application/json")
                                         .GET()
                                         .build();

        return client.send(img_req, HttpResponse.BodyHandlers.ofString());
    }

    private Product setProductByMap(Map<?, ?> p_val) throws
            IOException,
            InterruptedException {
        Product product = new Product();
        product.setId((int) p_val.get("id"));
        product.setSold((int) p_val.get("quantity"));
        product.setName((String) p_val.get("name"));
        product.setPrice((double) p_val.get("price"));

        HttpResponse<String> img_res = getImgByProductId(product.getId());
        product.setImageSrc(img_res.body());
        return product;
    }
}
