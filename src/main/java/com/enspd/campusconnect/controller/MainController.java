package com.enspd.campusconnect.controller;

import javafx.fxml.FXML;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Text pageTitle;
    @FXML
    private StackPane contentArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showDashboard();
    }

    @FXML
    public void showDashboard() {
        highlightButton("Dashboard");
        loadModule("/layout/dashboard.fxml", "Dashboard Overview");
    }


    private void highlightButton(String text) {
        // This is a simplified version. In a real app, you'd track the buttons.
        // For now, we'll assume the sidebar buttons are accessible or we iterate
        // children.
    }


    @FXML
    public void showStudents() {
        loadModule("/layout/etudiant.fxml", "Gestion des Étudiants");
    }

    @FXML
    public void showTeachers() {
        loadModule("/layout/enseignant.fxml", "Gestion des Enseignants");
    }

    @FXML
    public void showCourses() {
        loadModule("/layout/cours.fxml", "Gestion des Cours");
    }

    @FXML
    public void showGroups() {
        loadModule("/layout/groupe.fxml", "Gestion des Groupes");
    }

    @FXML
    public void showGrades() {
        loadModule("/layout/note.fxml", "Notes & Résultats");
    }

    @FXML
    public void showEnrollments() {
        loadModule("/layout/inscription.fxml", "Inscriptions");
    }

    @FXML
    public void showRooms() {
        loadModule("/layout/salle.fxml", "Gestion des Salles");
    }

    @FXML
    public void showSchedule() {
        loadModule("/layout/emploi_du_temps.fxml", "Emploi du Temps");
    }

    private void loadModule(String fxmlPath, String title) {
        try {
            pageTitle.setText(title);
            Node module = FXMLLoader.load(getClass().getResource(fxmlPath));

            // Animation: Fade In
            FadeTransition ft = new FadeTransition(Duration.millis(400), module);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);

            // Animation: Translate (Slide up)
            TranslateTransition tt = new TranslateTransition(Duration.millis(400), module);
            tt.setFromY(20);
            tt.setToY(0);

            contentArea.getChildren().setAll(module);

            ft.play();
            tt.play();

        } catch (Exception e) {
            e.printStackTrace();
            setPlaceholder(title + " (Erreur de chargement)");
        }
    }

    private void setPlaceholder(String module) {
        contentArea.getChildren().setAll(new Label("Module " + module + " en cours de développement..."));
    }
}
