package com.enspd.campusconnect.controller;

import com.enspd.campusconnect.model.Note;
import com.enspd.campusconnect.service.GradingService;
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

public class GradeController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<Note> gradeTable;
    @FXML
    private TableColumn<Note, String> colStudent;
    @FXML
    private TableColumn<Note, String> colCourse;
    @FXML
    private TableColumn<Note, String> colLibelle;
    @FXML
    private TableColumn<Note, Double> colValue;
    @FXML
    private TableColumn<Note, Double> colCoef;

    private GradingService gradingService;
    private ObservableList<Note> gradeList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize repositories
        var etudiantRepo = new com.enspd.campusconnect.repository.EtudiantRepository();
        var enseignantRepo = new com.enspd.campusconnect.repository.EnseignantRepository();
        var salleRepo = new com.enspd.campusconnect.repository.SalleRepository();
        var coursRepo = new com.enspd.campusconnect.repository.CoursRepository(enseignantRepo);
        var groupeRepo = new com.enspd.campusconnect.repository.GroupeRepository(coursRepo, enseignantRepo, salleRepo);
        var inscriptionRepo = new com.enspd.campusconnect.repository.InscriptionRepository(etudiantRepo, groupeRepo);
        var noteRepo = new com.enspd.campusconnect.repository.NoteRepository(inscriptionRepo);

        gradingService = new GradingService(noteRepo);

        colStudent.setCellValueFactory(cellData -> {
            var student = cellData.getValue().getInscription().getEtudiant();
            return new javafx.beans.property.SimpleStringProperty(
                    student != null ? student.getPrenom() + " " + student.getNom() : "N/A");
        });

        colCourse.setCellValueFactory(cellData -> {
            var group = cellData.getValue().getInscription().getGroupe();
            return new javafx.beans.property.SimpleStringProperty(
                    group != null ? group.getCode() : "N/A");
        });

        colLibelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colValue.setCellValueFactory(new PropertyValueFactory<>("valeur"));
        colCoef.setCellValueFactory(new PropertyValueFactory<>("coefficient"));

        loadGrades();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterGrades(newVal));
    }

    private void loadGrades() {
        gradeList.setAll(gradingService.listAll());
        gradeTable.setItems(gradeList);
    }

    private void filterGrades(String query) {
        if (query == null || query.isEmpty()) {
            gradeTable.setItems(gradeList);
            return;
        }

        ObservableList<Note> filtered = gradeList.filtered(n -> (n.getLibelle().toLowerCase()
                .contains(query.toLowerCase())) ||
                (n.getInscription().getEtudiant() != null &&
                        (n.getInscription().getEtudiant().getNom().toLowerCase().contains(query.toLowerCase()) ||
                                n.getInscription().getEtudiant().getPrenom().toLowerCase()
                                        .contains(query.toLowerCase())))
                ||
                (n.getInscription().getGroupe() != null
                        && n.getInscription().getGroupe().getCode().toLowerCase().contains(query.toLowerCase())));
        gradeTable.setItems(filtered);
    }

    @FXML
    private void handleNewGrade() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/layout/grade_form.fxml"));
            javafx.scene.Parent root = loader.load();
            GradeFormController controller = loader.getController();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Nouvelle Note");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);

            stage.showAndWait();

            if (controller.isSaveClicked()) {
                gradingService.addGrade(controller.getNote());
                loadGrades();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
