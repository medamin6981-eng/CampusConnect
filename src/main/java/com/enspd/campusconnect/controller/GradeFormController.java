package com.enspd.campusconnect.controller;

import com.enspd.campusconnect.model.Inscription;
import com.enspd.campusconnect.model.Note;
import com.enspd.campusconnect.repository.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class GradeFormController implements Initializable {

    @FXML private ComboBox<Inscription> enrollmentCombo;
    @FXML private TextField libelleField;
    @FXML private TextField gradeValueField;
    @FXML private Spinner<Double> coefSpinner;

    private Note note;
    private boolean saveClicked = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var ensRepo = new EnseignantRepository();
        var salleRepo = new SalleRepository();
        var coursRepo = new CoursRepository(ensRepo);
        var grpRepo = new GroupeRepository(coursRepo, ensRepo, salleRepo);
        var etuRepo = new EtudiantRepository();
        var inscriptionRepo = new InscriptionRepository(etuRepo, grpRepo);

        enrollmentCombo.setItems(FXCollections.observableArrayList(inscriptionRepo.findAll()));
        coefSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1.0, 5.0, 1.0));

        enrollmentCombo.setConverter(new StringConverter<Inscription>() {
            @Override
            public String toString(Inscription i) {
                if (i == null) return "";
                return i.getEtudiant().getNom() + " - " + i.getGroupe().getCode();
            }

            @Override
            public Inscription fromString(String s) { return null; }
        });
    }

    public boolean isSaveClicked() { return saveClicked; }

    public Note getNote() {
        double value = Double.parseDouble(gradeValueField.getText());
        if (note == null) {
            note = new Note(UUID.randomUUID().toString(), enrollmentCombo.getValue(), libelleField.getText(), value, coefSpinner.getValue());
        } else {
            // Updated Note model doesn't have setters for libelle/coef currently, 
            // but we can recreate or add them if needed. 
            // For now, let's assume we create a new one.
            note = new Note(note.getId(), enrollmentCombo.getValue(), libelleField.getText(), value, coefSpinner.getValue());
        }
        return note;
    }

    @FXML
    private void handleSave() {
        try {
            Double.parseDouble(gradeValueField.getText());
            if (enrollmentCombo.getValue() != null && !libelleField.getText().isEmpty()) {
                saveClicked = true;
                ((Stage) enrollmentCombo.getScene().getWindow()).close();
            }
        } catch (NumberFormatException e) {
            // Invalid number
        }
    }

    @FXML
    private void handleCancel() {
        ((Stage) enrollmentCombo.getScene().getWindow()).close();
    }
}
