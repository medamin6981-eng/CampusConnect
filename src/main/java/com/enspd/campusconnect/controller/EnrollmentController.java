package com.enspd.campusconnect.controller;

import com.enspd.campusconnect.model.Inscription;
import com.enspd.campusconnect.repository.InscriptionRepository;
import com.enspd.campusconnect.service.EnrollmentService;
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

public class EnrollmentController implements Initializable {

    @FXML private TextField searchField;
    @FXML private TableView<Inscription> enrollmentTable;
    @FXML private TableColumn<Inscription, String> colId;
    @FXML private TableColumn<Inscription, String> colStudent;
    @FXML private TableColumn<Inscription, String> colGroup;
    @FXML private TableColumn<Inscription, String> colDate;

    private EnrollmentService enrollmentService;
    private ObservableList<Inscription> enrollmentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var etuRepo = new com.enspd.campusconnect.repository.EtudiantRepository();
        var ensRepo = new com.enspd.campusconnect.repository.EnseignantRepository();
        var salleRepo = new com.enspd.campusconnect.repository.SalleRepository();
        var coursRepo = new com.enspd.campusconnect.repository.CoursRepository(ensRepo);
        var grpRepo = new com.enspd.campusconnect.repository.GroupeRepository(coursRepo, ensRepo, salleRepo);
        enrollmentService = new EnrollmentService(new InscriptionRepository(etuRepo, grpRepo));
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        colStudent.setCellValueFactory(cellData -> {
            var student = cellData.getValue().getEtudiant();
            return new javafx.beans.property.SimpleStringProperty(
                student != null ? student.getPrenom() + " " + student.getNom() : "N/A"
            );
        });
        
        colGroup.setCellValueFactory(cellData -> {
            var group = cellData.getValue().getGroupe();
            return new javafx.beans.property.SimpleStringProperty(
                group != null ? group.getCode() : "N/A"
            );
        });
        
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateInscription"));

        loadEnrollments();
        
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterEnrollments(newVal));
    }

    private void loadEnrollments() {
        enrollmentList.setAll(enrollmentService.listAll());
        enrollmentTable.setItems(enrollmentList);
    }

    private void filterEnrollments(String query) {
        if (query == null || query.isEmpty()) {
            enrollmentTable.setItems(enrollmentList);
            return;
        }
        
        ObservableList<Inscription> filtered = enrollmentList.filtered(i -> 
            (i.getId() != null && i.getId().toLowerCase().contains(query.toLowerCase())) ||
            (i.getEtudiant() != null && (i.getEtudiant().getNom().toLowerCase().contains(query.toLowerCase()) || 
                                       i.getEtudiant().getPrenom().toLowerCase().contains(query.toLowerCase()))) ||
            (i.getGroupe() != null && i.getGroupe().getCode().toLowerCase().contains(query.toLowerCase()))
        );
        enrollmentTable.setItems(filtered);
    }

    @FXML
    private void handleNewEnrollment() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/layout/enrollment_form.fxml"));
            javafx.scene.Parent root = loader.load();
            EnrollmentFormController controller = loader.getController();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Nouvelle Inscription");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);

            stage.showAndWait();

            if (controller.isSaveClicked()) {
                enrollmentService.enroll(controller.getInscription().getEtudiant(), controller.getInscription().getGroupe());
                loadEnrollments();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
