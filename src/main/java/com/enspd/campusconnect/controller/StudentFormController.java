package com.enspd.campusconnect.controller;

import com.enspd.campusconnect.model.Etudiant;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class StudentFormController implements Initializable {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField matriculeField;
    @FXML private DatePicker dateNaissanceField;
    @FXML private ComboBox<String> deptCombo;
    @FXML private ComboBox<String> anneeCombo;

    private Etudiant etudiant;
    private boolean saveClicked = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deptCombo.setItems(FXCollections.observableArrayList("Génie Civil", "Génie Électrique", "Génie Informatique", "Génie Mécanique", "Génie Industriel", "MSP"));
        anneeCombo.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5"));
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
        if (etudiant != null) {
            nomField.setText(etudiant.getNom());
            prenomField.setText(etudiant.getPrenom());
            emailField.setText(etudiant.getEmail());
            matriculeField.setText(etudiant.getMatricule());
            dateNaissanceField.setValue(etudiant.getDateNaissance());
            deptCombo.setValue(etudiant.getDepartement());
            anneeCombo.setValue(etudiant.getAnnee());
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public Etudiant getEtudiant() {
        if (etudiant == null) {
            etudiant = new Etudiant(UUID.randomUUID().toString(), nomField.getText(), prenomField.getText(), emailField.getText(), dateNaissanceField.getValue(), matriculeField.getText(), anneeCombo.getValue(), deptCombo.getValue());
        } else {
            etudiant.setNom(nomField.getText());
            etudiant.setPrenom(prenomField.getText());
            etudiant.setEmail(emailField.getText());
            etudiant.setMatricule(matriculeField.getText());
            etudiant.setDateNaissance(dateNaissanceField.getValue());
            etudiant.setDepartement(deptCombo.getValue());
            etudiant.setAnnee(anneeCombo.getValue());
        }
        return etudiant;
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
        // Basic validation logic
        return !nomField.getText().isEmpty() && !prenomField.getText().isEmpty() && dateNaissanceField.getValue() != null;
    }
}
