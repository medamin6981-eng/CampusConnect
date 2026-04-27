package com.enspd.campusconnect.controller;

import com.enspd.campusconnect.model.Seance;
import com.enspd.campusconnect.repository.*;
import com.enspd.campusconnect.service.ScheduleService;
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

public class ScheduleController implements Initializable {

    @FXML private TextField searchField;
    @FXML private TableView<Seance> scheduleTable;
    @FXML private TableColumn<Seance, String> colCourse;
    @FXML private TableColumn<Seance, String> colGroup;
    @FXML private TableColumn<Seance, String> colTeacher;
    @FXML private TableColumn<Seance, String> colRoom;
    @FXML private TableColumn<Seance, String> colStart;
    @FXML private TableColumn<Seance, String> colEnd;

    private ScheduleService scheduleService;
    private ObservableList<Seance> sessionList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Core repositories
        var ensRepo = new EnseignantRepository();
        var salleRepo = new SalleRepository();
        var coursRepo = new CoursRepository(ensRepo);
        var groupeRepo = new GroupeRepository(coursRepo, ensRepo, salleRepo);
        var seanceRepo = new SeanceRepository(coursRepo, groupeRepo, ensRepo, salleRepo);
        
        scheduleService = new ScheduleService(seanceRepo);
        
        colCourse.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCours().getIntitule()));
        colGroup.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getGroupe().getCode()));
        colTeacher.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getEnseignant().getPrenom() + " " + cellData.getValue().getEnseignant().getNom()));
        colRoom.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSalle().getCode()));
        colStart.setCellValueFactory(new PropertyValueFactory<>("debut"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("fin"));

        loadSessions();
        
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterSessions(newVal));
    }

    private void loadSessions() {
        sessionList.setAll(scheduleService.listAll());
        scheduleTable.setItems(sessionList);
    }

    private void filterSessions(String query) {
        if (query == null || query.isEmpty()) {
            scheduleTable.setItems(sessionList);
            return;
        }
        
        ObservableList<Seance> filtered = sessionList.filtered(s -> 
            s.getCours().getIntitule().toLowerCase().contains(query.toLowerCase()) ||
            s.getSalle().getCode().toLowerCase().contains(query.toLowerCase()) ||
            s.getEnseignant().getNom().toLowerCase().contains(query.toLowerCase())
        );
        scheduleTable.setItems(filtered);
    }

    @FXML
    private void handleNewSession() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/layout/schedule_form.fxml"));
            javafx.scene.Parent root = loader.load();
            ScheduleFormController controller = loader.getController();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Planifier une Séance");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);

            stage.showAndWait();

            if (controller.isSaveClicked()) {
                scheduleService.addSession(controller.getSeance());
                loadSessions();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
