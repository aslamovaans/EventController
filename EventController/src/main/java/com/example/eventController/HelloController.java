package com.example.eventController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.h2.message.Message;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    public Slider equipmentCountText;
    public ListView<Guest> guestsList;
    public TextField eventTitleText;
    public TextField eventPlaceText;
    public TextField eventDateText;
    public ListView<Equipment> equipmentList;
    public ListView<Event> eventsList;
    public ListView<EquipmentPeople> userEventsEquipmentList;
    public ListView<GuestPeople> userEventsGuestList;
    People currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void init(int id) {

        DatabaseAdapter.getDBConnection();
        currentUser = DatabaseAdapter.getUser(id);
        DatabaseAdapter.getEquipmentForPeople(userEventsEquipmentList.getItems(),currentUser);
        DatabaseAdapter.getGuestsForPeople(userEventsGuestList.getItems(),currentUser);
        DatabaseAdapter.getEvents(eventsList.getItems());

        eventsList.getSelectionModel().selectedItemProperty().addListener(e->{
            if(eventsList.getSelectionModel().getSelectedIndex()>-1) {
                equipmentList.getItems().clear();
                guestsList.getItems().clear();
                eventTitleText.setText(eventsList.getSelectionModel().getSelectedItem().title);
                eventPlaceText.setText(eventsList.getSelectionModel().getSelectedItem().place);
                eventDateText.setText(eventsList.getSelectionModel().getSelectedItem().dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
                DatabaseAdapter.getEquipmentForEvent(equipmentList.getItems(), eventsList.getSelectionModel().getSelectedItem());
                DatabaseAdapter.getGuestsForEvent(guestsList.getItems(), eventsList.getSelectionModel().getSelectedItem());
            }
        });
        equipmentList.getSelectionModel().selectedItemProperty().addListener(e->{
            if(equipmentList.getSelectionModel().getSelectedIndex()>-1)
                equipmentCountText.setMax(equipmentList.getSelectionModel().getSelectedItem().count_need-equipmentList.getSelectionModel().getSelectedItem().count_exist);
        });
    }
    @FXML
    private void goToEvent() {
        if (guestsList.getSelectionModel().getSelectedIndex() > -1) {
            if (guestsList.getSelectionModel().getSelectedItem().count_need - guestsList.getSelectionModel().getSelectedItem().count_exist > 0) {
                userEventsGuestList.getItems().clear();
                DatabaseAdapter.fillGuest(currentUser, guestsList.getSelectionModel().getSelectedItem());
                DatabaseAdapter.getGuestsForPeople(userEventsGuestList.getItems(), currentUser);
                guestsList.getItems().clear();
                DatabaseAdapter.getGuestsForEvent(guestsList.getItems(), eventsList.getSelectionModel().getSelectedItem());
            } else new Alert(Alert.AlertType.ERROR,"Участники больше не требуются!").show();
        }

    }
    @FXML
    private void equipForEvent()
    {
        if(equipmentList.getSelectionModel().getSelectedIndex()>-1) {
            if (equipmentList.getSelectionModel().getSelectedItem().count_need - equipmentList.getSelectionModel().getSelectedItem().count_exist > 0) {
                userEventsEquipmentList.getItems().clear();
                DatabaseAdapter.fillEquipment(currentUser, equipmentList.getSelectionModel().getSelectedItem(), (int) equipmentCountText.getValue());
                DatabaseAdapter.getEquipmentForPeople(userEventsEquipmentList.getItems(), currentUser);
                equipmentList.getItems().clear();
                DatabaseAdapter.getEquipmentForEvent(equipmentList.getItems(), eventsList.getSelectionModel().getSelectedItem());
            }else new Alert(Alert.AlertType.ERROR,"Оборудование больше не требуется!").show();
        }
    }
    @FXML
    public void createEvent() throws IOException {
        equipmentList.getScene().getWindow().hide();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("event-creating.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((CreatingController)fxmlLoader.getController()).init(currentUser.id);
        stage.setTitle("Создание мероприятия");
        stage.setScene(scene);
        stage.show();
    }
}
