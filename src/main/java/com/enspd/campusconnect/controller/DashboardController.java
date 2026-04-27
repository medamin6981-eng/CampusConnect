package com.enspd.campusconnect.controller;

import com.enspd.campusconnect.util.DatabaseManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Label studentCount;
    @FXML
    private Label teacherCount;
    @FXML
    private Label courseCount;
    @FXML
    private Label groupCount;
    @FXML
    private Label roomCount;

    @FXML
    private PieChart enrollmentChart;
    @FXML
    private LineChart<String, Number> gradesChart;
    @FXML
    private BarChart<String, Number> successChart;

    @FXML
    private VBox activityList;
    @FXML
    private VBox alertsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshData();
    }

    public void refreshData() {
        Platform.runLater(() -> {
            loadKPIs();
            loadCharts();
            loadRecentActivity();
            loadAlerts();
        });
    }

    private void loadKPIs() {
        studentCount.setText(String.valueOf(getCount("etudiants")));
        teacherCount.setText(String.valueOf(getCount("enseignants")));
        courseCount.setText(String.valueOf(getCount("cours")));
        groupCount.setText(String.valueOf(getCount("groupes")));
        roomCount.setText(String.valueOf(getCount("salles")));
    }

    private void loadCharts() {
        int enrolled = getCount("inscriptions");
        int totalCap = 200; // Simplified capacity

        enrollmentChart.getData().setAll(
                new PieChart.Data("Inscrits", enrolled),
                new PieChart.Data("Places Libres", Math.max(0, totalCap - enrolled)));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Moyennes Semestrielles");
        series.getData().add(new XYChart.Data<>("Sem 1", 12.8));
        series.getData().add(new XYChart.Data<>("Sem 2", 13.5));
        gradesChart.getData().setAll(series);

        XYChart.Series<String, Number> successSeries = new XYChart.Series<>();
        successSeries.setName("Réussite %");
        successSeries.getData().add(new XYChart.Data<>("INFO", 85));
        successSeries.getData().add(new XYChart.Data<>("GC", 78));
        successChart.getData().setAll(successSeries);
    }

    private void loadRecentActivity() {
        activityList.getChildren().clear();
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT e.nom, i.date_inscription FROM inscriptions i JOIN etudiants e ON i.etudiant_id = e.id ORDER BY i.id DESC LIMIT 3")) {
            while (rs.next()) {
                addActivityItem("Inscription : " + rs.getString("nom") + " (" + rs.getString("date_inscription") + ")");
            }
        } catch (SQLException e) {
        }

        if (activityList.getChildren().isEmpty()) {
            addActivityItem("Aucune activité récente.");
        }
    }

    private void loadAlerts() {
        alertsList.getChildren().clear();

        // Alert: Cours sans enseignant
        int coursesNoTeacher = getCountWhere("cours", "responsable_id IS NULL");
        if (coursesNoTeacher > 0) {
            addAlertItem("⚠️ " + coursesNoTeacher + " cours n'ont pas d'enseignant assigné.");
        }

        // Alert: Groupes sans salle
        int groupsNoRoom = getCountWhere("groupes", "salle_id IS NULL");
        if (groupsNoRoom > 0) {
            addAlertItem("⚠️ " + groupsNoRoom + " groupes n'ont pas de salle assignée.");
        }

        if (alertsList.getChildren().isEmpty()) {
            Label ok = new Label("✓ Toutes les ressources sont assignées.");
            ok.setStyle("-fx-text-fill: #C6F065;");
            alertsList.getChildren().add(ok);
        }
    }

    private void addActivityItem(String text) {
        Label label = new Label("• " + text);
        label.getStyleClass().add("activity-item");
        activityList.getChildren().add(label);
    }

    private void addAlertItem(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("alert-item");
        alertsList.getChildren().add(label);
    }

    private int getCount(String tableName) {
        return getCountWhere(tableName, null);
    }

    private int getCountWhere(String tableName, String condition) {
        String query = "SELECT COUNT(*) FROM " + tableName + (condition != null ? " WHERE " + condition : "");
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
        }
        return 0;
    }
}
