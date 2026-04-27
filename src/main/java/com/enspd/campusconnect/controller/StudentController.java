package com.enspd.campusconnect.controller;

import com.enspd.campusconnect.model.Etudiant;
import com.enspd.campusconnect.repository.EtudiantRepository;
import com.enspd.campusconnect.service.StudentService;
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

public class StudentController implements Initializable {

    @FXML private TextField searchField;
    @FXML private TableView<Etudiant> studentTable;
    @FXML private TableColumn<Etudiant, String> colId;
    @FXML private TableColumn<Etudiant, String> colMatricule;
    @FXML private TableColumn<Etudiant, String> colNom;
    @FXML private TableColumn<Etudiant, String> colPrenom;
    @FXML private TableColumn<Etudiant, String> colEmail;
    @FXML private TableColumn<Etudiant, String> colDepartement;
    @FXML private TableColumn<Etudiant, String> colAnnee;

    private StudentService studentService;
    private ObservableList<Etudiant> studentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        studentService = new StudentService(new EtudiantRepository());
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDepartement.setCellValueFactory(new PropertyValueFactory<>("departement"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("annee"));

        loadStudents();
    }

    private void loadStudents() {
        studentList.setAll(studentService.listAll());
        studentTable.setItems(studentList);
    }

    @FXML
    private void handleAddStudent() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/layout/student_form.fxml"));
            javafx.scene.Parent root = loader.load();
            StudentFormController controller = loader.getController();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Nouvel Étudiant");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);

            stage.showAndWait();

            if (controller.isSaveClicked()) {
                studentService.addStudent(controller.getEtudiant());
                loadStudents();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
