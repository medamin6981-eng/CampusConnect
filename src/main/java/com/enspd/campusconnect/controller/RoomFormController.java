package com.enspd.campusconnect.controller;

import com.enspd.campusconnect.model.Salle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomFormController implements Initializable {

    @FXML private TextField codeField;
    @FXML private Spinner<Integer> capacitySpinner;
    @FXML private ComboBox<Salle.TypeSalle> typeCombo;

    private Salle salle;
    private boolean saveClicked = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        capacitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, 30));
        typeCombo.setItems(FXCollections.observableArrayList(Salle.TypeSalle.values()));
    }

    public boolean isSaveClicked() { return saveClicked; }

    public Salle getSalle() {
        if (salle == null) {
            salle = new Salle(codeField.getText(), capacitySpinner.getValue(), typeCombo.getValue());
        } else {
            salle.setCapacite(capacitySpinner.getValue());
            salle.setType(typeCombo.getValue());
        }
        return salle;
    }

    @FXML
    private void handleSave() {
        if (!codeField.getText().isEmpty() && typeCombo.getValue() != null) {
            saveClicked = true;
            ((Stage) codeField.getScene().getWindow()).close();
        }
    }

    @FXML
    private void handleCancel() {
        ((Stage) codeField.getScene().getWindow()).close();
    }
}
