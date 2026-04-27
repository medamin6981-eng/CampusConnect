package com.enspd.campusconnect.controller;

import com.enspd.campusconnect.model.Enseignant;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.UUID;

public class TeacherFormController implements Initializable {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private DatePicker birthDatePicker;
    @FXML private ComboBox<Enseignant.Statut> statutCombo;
    @FXML private ComboBox<String> deptCombo;

    private Enseignant enseignant;
    private boolean saveClicked = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statutCombo.setItems(FXCollections.observableArrayList(Enseignant.Statut.values()));
        deptCombo.setItems(FXCollections.observableArrayList("Génie Civil", "Génie Électrique", "Génie Informatique", "Génie Mécanique", "Génie Industriel", "MSP"));
        birthDatePicker.setValue(LocalDate.of(1980, 1, 1));
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
        if (enseignant != null) {
            nomField.setText(enseignant.getNom());
            prenomField.setText(enseignant.getPrenom());
            emailField.setText(enseignant.getEmail());
            birthDatePicker.setValue(enseignant.getDateNaissance());
            statutCombo.setValue(enseignant.getStatut());
            deptCombo.setValue(enseignant.getDepartement());
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public Enseignant getEnseignant() {
        if (enseignant == null) {
            enseignant = new Enseignant(UUID.randomUUID().toString(), nomField.getText(), prenomField.getText(), emailField.getText(), birthDatePicker.getValue(), statutCombo.getValue(), deptCombo.getValue());
        } else {
            enseignant.setNom(nomField.getText());
            enseignant.setPrenom(prenomField.getText());
            enseignant.setEmail(emailField.getText());
            enseignant.setStatut(statutCombo.getValue());
            enseignant.setDepartement(deptCombo.getValue());
        }
        return enseignant;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            saveClicked = true;
            ((Stage) nomField.getScene().getWindow()).close();
        }
    }

    @FXML
    private void handleCancel() {
        ((Stage) nomField.getScene().getWindow()).close();
    }

    private boolean isInputValid() {
        return !nomField.getText().isEmpty() && !prenomField.getText().isEmpty() && !emailField.getText().isEmpty() && birthDatePicker.getValue() != null && statutCombo.getValue() != null;
    }
}
