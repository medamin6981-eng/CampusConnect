package com.enspd.campusconnect.controller;

import com.enspd.campusconnect.model.*;
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

public class GroupeFormController implements Initializable {

    @FXML
    private TextField codeField;
    @FXML
    private ComboBox<Groupe.TypeGroupe> typeCombo;
    @FXML
    private ComboBox<Cours> courseCombo;
    @FXML
    private ComboBox<Enseignant> teacherCombo;
    @FXML
    private ComboBox<Salle> roomCombo;
    @FXML
    private Spinner<Integer> capacitySpinner;

    private Groupe groupe;
    private boolean saveClicked = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var ensRepo = new EnseignantRepository();
        var salleRepo = new SalleRepository();
        var coursRepo = new CoursRepository(ensRepo);

        typeCombo.setItems(FXCollections.observableArrayList(Groupe.TypeGroupe.values()));
        courseCombo.setItems(FXCollections.observableArrayList(coursRepo.findAll()));
        teacherCombo.setItems(FXCollections.observableArrayList(ensRepo.findAll()));
        roomCombo.setItems(FXCollections.observableArrayList(salleRepo.findAll()));

        capacitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 100, 30));

        // Converters
        courseCombo.setConverter(createConverter(Cours::getIntitule));
        teacherCombo.setConverter(createConverter(t -> t.getPrenom() + " " + t.getNom()));
        roomCombo.setConverter(createConverter(Salle::getCode));
    }

    private <T> StringConverter<T> createConverter(java.util.function.Function<T, String> mapper) {
        return new StringConverter<T>() {
            @Override
            public String toString(T object) {
                return object != null ? mapper.apply(object) : "";
            }

            @Override
            public T fromString(String string) {
                return null;
            }
        };
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public Groupe getGroupe() {
        if (groupe == null) {
            groupe = new Groupe(codeField.getText(), typeCombo.getValue(), capacitySpinner.getValue(),
                    courseCombo.getValue());
            groupe.setEnseignant(teacherCombo.getValue());
            groupe.setSalle(roomCombo.getValue());
        } else {
            groupe.setCapaciteMax(capacitySpinner.getValue());
            groupe.setEnseignant(teacherCombo.getValue());
            groupe.setSalle(roomCombo.getValue());
        }
        return groupe;
    }

    @FXML
    private void handleSave() {
        if (!codeField.getText().isEmpty() && typeCombo.getValue() != null && courseCombo.getValue() != null) {
            saveClicked = true;
            ((Stage) codeField.getScene().getWindow()).close();
        }
    }

    @FXML
    private void handleCancel() {
        ((Stage) codeField.getScene().getWindow()).close();
    }
}
