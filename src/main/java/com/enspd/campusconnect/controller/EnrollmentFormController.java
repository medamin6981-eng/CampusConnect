package com.enspd.campusconnect.controller;

import com.enspd.campusconnect.model.Etudiant;
import com.enspd.campusconnect.model.Groupe;
import com.enspd.campusconnect.model.Inscription;
import com.enspd.campusconnect.repository.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.UUID;

public class EnrollmentFormController implements Initializable {

    @FXML private ComboBox<Etudiant> studentCombo;
    @FXML private ComboBox<Groupe> groupCombo;
    @FXML private DatePicker dateField;

    private Inscription inscription;
    private boolean saveClicked = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var ensRepo = new EnseignantRepository();
        var salleRepo = new SalleRepository();
        var coursRepo = new CoursRepository(ensRepo);
        var etuRepo = new EtudiantRepository();
        var grpRepo = new GroupeRepository(coursRepo, ensRepo, salleRepo);

        studentCombo.setItems(FXCollections.observableArrayList(etuRepo.findAll()));
        groupCombo.setItems(FXCollections.observableArrayList(grpRepo.findAll()));
        dateField.setValue(LocalDate.now());

        studentCombo.setConverter(new StringConverter<Etudiant>() {
            @Override public String toString(Etudiant e) { return e != null ? e.getPrenom() + " " + e.getNom() : ""; }
            @Override public Etudiant fromString(String s) { return null; }
        });

        groupCombo.setConverter(new StringConverter<Groupe>() {
            @Override public String toString(Groupe g) { return g != null ? g.getCode() : ""; }
            @Override public Groupe fromString(String s) { return null; }
        });
    }

    public boolean isSaveClicked() { return saveClicked; }

    public Inscription getInscription() {
        // Inscription model doesn't support setting etudiant/groupe/date after creation
        // So we recreate it every time if it's new or edited from this form.
        inscription = new Inscription(UUID.randomUUID().toString(), studentCombo.getValue(), groupCombo.getValue());
        return inscription;
    }

    @FXML
    private void handleSave() {
        if (studentCombo.getValue() != null && groupCombo.getValue() != null) {
            saveClicked = true;
            ((Stage) studentCombo.getScene().getWindow()).close();
        }
    }

    @FXML
    private void handleCancel() {
        ((Stage) studentCombo.getScene().getWindow()).close();
    }
}
