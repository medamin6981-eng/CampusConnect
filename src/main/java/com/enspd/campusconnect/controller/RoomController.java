package com.enspd.campusconnect.controller;

import com.enspd.campusconnect.model.Salle;
import com.enspd.campusconnect.repository.SalleRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomController implements Initializable {

    @FXML private TextField searchField;
    @FXML private TableView<Salle> roomTable;
    @FXML private TableColumn<Salle, String> colCode;
    @FXML private TableColumn<Salle, Integer> colCapacite;
    @FXML private TableColumn<Salle, String> colType;

    private SalleRepository roomRepository;
    private ObservableList<Salle> roomList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roomRepository = new SalleRepository();
        
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colCapacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        colType.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType().name())
        );

        loadRooms();
        
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterRooms(newVal));
    }

    private void loadRooms() {
        roomList.setAll(roomRepository.findAll());
        roomTable.setItems(roomList);
    }

    private void filterRooms(String query) {
        if (query == null || query.isEmpty()) {
            roomTable.setItems(roomList);
            return;
        }
        
        ObservableList<Salle> filtered = roomList.filtered(s -> 
            s.getCode().toLowerCase().contains(query.toLowerCase()) ||
            s.getType().name().toLowerCase().contains(query.toLowerCase())
        );
        roomTable.setItems(filtered);
    }

    @FXML
    private void handleAddRoom() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/layout/room_form.fxml"));
            javafx.scene.Parent root = loader.load();
            RoomFormController controller = loader.getController();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Nouvelle Salle");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);

            stage.showAndWait();

            if (controller.isSaveClicked()) {
                roomRepository.save(controller.getSalle());
                loadRooms();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
