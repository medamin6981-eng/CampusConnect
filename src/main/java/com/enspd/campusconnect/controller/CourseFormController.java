package com.enspd.campusconnect.controller;

import com.enspd.campusconnect.model.Cours;
import com.enspd.campusconnect.model.Enseignant;
import com.enspd.campusconnect.repository.EnseignantRepository;
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

public class CourseFormController implements Initializable {

    @FXML private TextField codeField;
    @FXML private TextField intituleField;
    @FXML private Spinner<Integer> vhSpinner;
    @FXML private ComboBox<Enseignant> teacherCombo;

    private Cours cours;
    private boolean saveClicked = false;
    private EnseignantRepository teacherRepo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        teacherRepo = new EnseignantRepository();
        
        vhSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 100, 30));
        
        teacherCombo.setItems(FXCollections.observableArrayList(teacherRepo.findAll()));
        teacherCombo.setConverter(new StringConverter<Enseignant>() {
            @Override
            public String toString(Enseignant teacher) {
                return teacher != null ? teacher.getPrenom() + " " + teacher.getNom() : "";
            }

            @Override
            public Enseignant fromString(String string) {
                return null; // Not needed
            }
        });
    }

    public void setCours(Cours cours) {
        this.cours = cours;
        if (cours != null) {
            codeField.setText(cours.getCode());
            intituleField.setText(cours.getIntitule());
            vhSpinner.getValueFactory().setValue(cours.getVolumeHoraire());
            teacherCombo.setValue(cours.getResponsable());
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public Cours getCours() {
        if (cours == null) {
            cours = new Cours(codeField.getText(), intituleField.getText(), vhSpinner.getValue(), teacherCombo.getValue());
        } else {
            cours.setIntitule(intituleField.getText());
            cours.setVolumeHoraire(vhSpinner.getValue());
            cours.setResponsable(teacherCombo.getValue());
        }
        return cours;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            saveClicked = true;
            ((Stage) codeField.getScene().getWindow()).close();
        }
    }

    @FXML
    private void handleCancel() {
        ((Stage) codeField.getScene().getWindow()).close();
    }

    private boolean isInputValid() {
        return !codeField.getText().isEmpty() && !intituleField.getText().isEmpty() && teacherCombo.getValue() != null;
    }
}
