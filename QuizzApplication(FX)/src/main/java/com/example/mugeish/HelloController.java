package com.example.mugeish;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Label Category;

    @FXML
    private ChoiceBox<String> myChoiceBox;

    private final String[] items = {"Select Category","English", "Science"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (myChoiceBox == null) {
            return;
        }
        myChoiceBox.getItems().addAll(items);
        myChoiceBox.setValue(items[0]);
        myChoiceBox.setOnAction(this::getCategory);
    }

    public void getCategory (ActionEvent event){
        String getItem = myChoiceBox.getValue();
        Category.setText(getItem);
    }


}