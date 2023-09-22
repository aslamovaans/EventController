package com.example.eventController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreatingController implements Initializable {
    public TextArea guestDescriptionText;
    public Slider guestCountText;
    public ListView<Guest> guestList;
    public Slider equipmentCountText;
    public TextField equipmentTitleText;
    public ListView<Equipment> equipmentList;
    public DatePicker dateText;
    public TextField locationText;
    public TextField titleText;
    People currentPeople;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void init(int id) {
        DatabaseAdapter.getDBConnection();
        currentPeople = DatabaseAdapter.getUser(id);
    }
    @FXML
    private void addEquipment()
    {
        equipmentList.getItems().add(new Equipment(equipmentTitleText.getText(), (int) equipmentCountText.getValue(),0));
    }
    @FXML
    private void addGuest()
    {
        guestList.getItems().add(new Guest(-1,guestDescriptionText.getText(), (int) guestCountText.getValue(),0));
    }
    @FXML
    private void deleteGuest()
    {
        if(guestList.getSelectionModel().getSelectedIndex()>-1)
            guestList.getItems().remove(guestList.getSelectionModel().getSelectedItem());
    }
    @FXML
    private void deleteEquipment()
    {
        if(equipmentList.getSelectionModel().getSelectedIndex()>-1)
            equipmentList.getItems().remove(equipmentList.getSelectionModel().getSelectedItem());
    }
    @FXML
    private void create() throws IOException {
        Event event = new Event.RequestBuilder(dateText.getValue().atStartOfDay(),titleText.getText(),locationText.getText()).build();

        event.setId(DatabaseAdapter.addEvent(event));
        for (Guest guest: guestList.getItems()) {
            guest.setId_event(event.id);
            DatabaseAdapter.addGuest(guest);
        }
        for (Equipment equipment: equipmentList.getItems()) {
            equipment.setId_event(event.id);
            DatabaseAdapter.addEquipment(equipment);
        }
        guestDescriptionText.getScene().getWindow().hide();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((HelloController)fxmlLoader.getController()).init(currentPeople.id);
        stage.setTitle("Панель пользователя");
        stage.setScene(scene);
        stage.show();
    }

}
