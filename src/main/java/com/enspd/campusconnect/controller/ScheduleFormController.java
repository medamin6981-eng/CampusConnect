package com.enspd.campusconnect.controller;

import com.enspd.campusconnect.model.*;
import com.enspd.campusconnect.repository.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.UUID;

public class ScheduleFormController implements Initializable {

    @FXML private ComboBox<Cours> courseCombo;
    @FXML private ComboBox<Groupe> groupCombo;
    @FXML private ComboBox<Enseignant> teacherCombo;
    @FXML private ComboBox<Salle> roomCombo;
    @FXML private DatePicker datePicker;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;

    private Seance seance;
    private boolean saveClicked = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var ensRepo = new EnseignantRepository();
        var salleRepo = new SalleRepository();
        var coursRepo = new CoursRepository(ensRepo);
        var grpRepo = new GroupeRepository(coursRepo, ensRepo, salleRepo);

        courseCombo.setItems(FXCollections.observableArrayList(coursRepo.findAll()));
        groupCombo.setItems(FXCollections.observableArrayList(grpRepo.findAll()));
        teacherCombo.setItems(FXCollections.observableArrayList(ensRepo.findAll()));
        roomCombo.setItems(FXCollections.observableArrayList(salleRepo.findAll()));
        datePicker.setValue(LocalDate.now());

        // Converters
        courseCombo.setConverter(new StringConverter<Cours>() {
            @Override public String toString(Cours c) { return c != null ? c.getCode() + " - " + c.getIntitule() : ""; }
            @Override public Cours fromString(String s) { return null; }
        });
        groupCombo.setConverter(new StringConverter<Groupe>() {
            @Override public String toString(Groupe g) { return g != null ? g.getCode() : ""; }
            @Override public Groupe fromString(String s) { return null; }
        });
        teacherCombo.setConverter(new StringConverter<Enseignant>() {
            @Override public String toString(Enseignant t) { return t != null ? t.getPrenom() + " " + t.getNom() : ""; }
            @Override public Enseignant fromString(String s) { return null; }
        });
        roomCombo.setConverter(new StringConverter<Salle>() {
            @Override public String toString(Salle r) { return r != null ? r.getCode() : ""; }
            @Override public Salle fromString(String s) { return null; }
        });
    }

    public boolean isSaveClicked() { return saveClicked; }

    public Seance getSeance() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime start = LocalDateTime.of(datePicker.getValue(), LocalTime.parse(startTimeField.getText(), timeFormatter));
        LocalDateTime end = LocalDateTime.of(datePicker.getValue(), LocalTime.parse(endTimeField.getText(), timeFormatter));
        
        if (seance == null) {
            seance = new Seance(UUID.randomUUID().toString(), courseCombo.getValue(), groupCombo.getValue(), teacherCombo.getValue(), roomCombo.getValue(), start, end);
        } else {
            seance.setCours(courseCombo.getValue());
            seance.setGroupe(groupCombo.getValue());
            seance.setEnseignant(teacherCombo.getValue());
            seance.setSalle(roomCombo.getValue());
            seance.setDebut(start);
            seance.setFin(end);
        }
        return seance;
    }

    @FXML
    private void handleSave() {
        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime.parse(startTimeField.getText(), timeFormatter);
            LocalTime.parse(endTimeField.getText(), timeFormatter);
            
            if (courseCombo.getValue() != null && groupCombo.getValue() != null && teacherCombo.getValue() != null && roomCombo.getValue() != null) {
                saveClicked = true;
                ((Stage) courseCombo.getScene().getWindow()).close();
            }
        } catch (Exception e) {
            // Invalid time format
        }
    }

    @FXML
    private void handleCancel() {
        ((Stage) courseCombo.getScene().getWindow()).close();
    }
}
