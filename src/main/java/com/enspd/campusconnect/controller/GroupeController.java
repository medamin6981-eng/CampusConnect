package com.enspd.campusconnect.controller;

import com.enspd.campusconnect.model.*;
import com.enspd.campusconnect.repository.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GroupeController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<Groupe> groupTable;
    @FXML
    private TableColumn<Groupe, String> colCode;
    @FXML
    private TableColumn<Groupe, String> colCourse;
    @FXML
    private TableColumn<Groupe, String> colTeacher;
    @FXML
    private TableColumn<Groupe, String> colRoom;
    @FXML
    private TableColumn<Groupe, Integer> colCapacity;
    @FXML
    private TableColumn<Groupe, Integer> colEnrolled;

    private GroupeRepository groupRepo;
    private ObservableList<Groupe> groupList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var ensRepo = new EnseignantRepository();
        var salleRepo = new SalleRepository();
        var coursRepo = new CoursRepository(ensRepo);
        groupRepo = new GroupeRepository(coursRepo, ensRepo, salleRepo);

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colCourse.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getCours().getIntitule()));
        colTeacher.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getEnseignant().getPrenom() + " " + cellData.getValue().getEnseignant().getNom()));
        colRoom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSalle().getCode()));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capaciteMax"));
        colEnrolled.setCellValueFactory(new PropertyValueFactory<>("nbInscrits"));

        loadGroups();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterGroups(newVal));
    }

    private void loadGroups() {
        groupList.setAll(groupRepo.findAll());
        groupTable.setItems(groupList);
    }

    private void filterGroups(String query) {
        if (query == null || query.isEmpty()) {
            groupTable.setItems(groupList);
            return;
        }
        ObservableList<Groupe> filtered = groupList
                .filtered(g -> g.getCode().toLowerCase().contains(query.toLowerCase()) ||
                        g.getCours().getIntitule().toLowerCase().contains(query.toLowerCase()));
        groupTable.setItems(filtered);
    }

    @FXML
    private void handleAddGroup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/groupe_form.fxml"));
            Parent root = loader.load();
            GroupeFormController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Créer un Groupe");
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);

            stage.showAndWait();

            if (controller.isSaveClicked()) {
                groupRepo.save(controller.getGroupe());
                loadGroups();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
