package com.enspd.campusconnect.controller;

import com.enspd.campusconnect.model.Enseignant;
import com.enspd.campusconnect.repository.EnseignantRepository;
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

public class TeacherController implements Initializable {

    @FXML private TextField searchField;
    @FXML private TableView<Enseignant> teacherTable;
    @FXML private TableColumn<Enseignant, String> colId;
    @FXML private TableColumn<Enseignant, String> colNom;
    @FXML private TableColumn<Enseignant, String> colPrenom;
    @FXML private TableColumn<Enseignant, String> colEmail;
    @FXML private TableColumn<Enseignant, String> colDepartement;
    @FXML private TableColumn<Enseignant, String> colStatut;

    private EnseignantRepository repository;
    private ObservableList<Enseignant> teacherList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repository = new EnseignantRepository();
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDepartement.setCellValueFactory(new PropertyValueFactory<>("departement"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        loadTeachers();
    }

    private void loadTeachers() {
        teacherList.setAll(repository.findAll());
        teacherTable.setItems(teacherList);
    }

    @FXML
    private void handleAddTeacher() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/layout/teacher_form.fxml"));
            javafx.scene.Parent root = loader.load();
            TeacherFormController controller = loader.getController();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Nouvel Enseignant");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);

            stage.showAndWait();

            if (controller.isSaveClicked()) {
                repository.save(controller.getEnseignant());
                loadTeachers();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
