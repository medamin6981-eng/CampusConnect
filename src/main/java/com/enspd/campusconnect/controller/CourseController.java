package com.enspd.campusconnect.controller;

import com.enspd.campusconnect.model.Cours;
import com.enspd.campusconnect.repository.CoursRepository;
import com.enspd.campusconnect.service.CourseService;
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

public class CourseController implements Initializable {

    @FXML private TextField searchField;
    @FXML private TableView<Cours> courseTable;
    @FXML private TableColumn<Cours, String> colCode;
    @FXML private TableColumn<Cours, String> colIntitule;
    @FXML private TableColumn<Cours, Integer> colVH;
    @FXML private TableColumn<Cours, String> colResponsable;

    private CourseService courseService;
    private ObservableList<Cours> courseList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var ensRepo = new com.enspd.campusconnect.repository.EnseignantRepository();
        courseService = new CourseService(new CoursRepository(ensRepo));
        
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colIntitule.setCellValueFactory(new PropertyValueFactory<>("intitule"));
        colVH.setCellValueFactory(new PropertyValueFactory<>("volumeHoraire"));
        
        // Custom value factory for responsible teacher name
        colResponsable.setCellValueFactory(cellData -> {
            var resp = cellData.getValue().getResponsable();
            return new javafx.beans.property.SimpleStringProperty(
                resp != null ? resp.getPrenom() + " " + resp.getNom() : "N/A"
            );
        });

        loadCourses();
        
        // Add search listener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterCourses(newValue);
        });
    }

    private void loadCourses() {
        courseList.setAll(courseService.listAll());
        courseTable.setItems(courseList);
    }

    private void filterCourses(String query) {
        if (query == null || query.isEmpty()) {
            courseTable.setItems(courseList);
            return;
        }
        
        ObservableList<Cours> filtered = courseList.filtered(c -> 
            c.getCode().toLowerCase().contains(query.toLowerCase()) ||
            c.getIntitule().toLowerCase().contains(query.toLowerCase())
        );
        courseTable.setItems(filtered);
    }

    @FXML
    private void handleAddCourse() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/layout/course_form.fxml"));
            javafx.scene.Parent root = loader.load();
            CourseFormController controller = loader.getController();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Nouveau Cours");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);

            stage.showAndWait();

            if (controller.isSaveClicked()) {
                courseService.save(controller.getCours());
                loadCourses();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
