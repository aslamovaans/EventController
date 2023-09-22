package com.example.eventController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable {
    public TextField nameText;
    public TextField passwordText;
    public Button registerButton;
    public TextField phoneText;
    int id;
    @FXML
    private void login() throws IOException {
        if(userCheck())
            openUserMenu();
        else
            new Alert(Alert.AlertType.ERROR,"Пользователь не найден!", ButtonType.OK).show();
    }
    @FXML
    private void register() throws IOException {
        DatabaseAdapter.addUser(new People(nameText.getText(), passwordText.getText()).setPhone(phoneText.getText()));
        if(userCheck())
            openUserMenu();
        else new Alert(Alert.AlertType.ERROR,"Пользователь не создан!", ButtonType.OK).show();
    }


    private boolean userCheck()
    {
        id = DatabaseAdapter.getUser(nameText.getText(),passwordText.getText());
        return id != -1;
    }
    private void openUserMenu() throws IOException {
        nameText.getScene().getWindow().hide();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((HelloController)fxmlLoader.getController()).init(id);
        stage.setTitle("Панель пользователя");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseAdapter.getDBConnection();
    }
}
