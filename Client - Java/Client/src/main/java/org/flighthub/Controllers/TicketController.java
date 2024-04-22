package org.flighthub.Controllers;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class TicketController {
    @FXML
    private TextField clientNameTextField;
    @FXML
    private TextField clientAddressTextField;
    @FXML
    private TextField touristsNameTextField;
    @FXML
    private TextField seatsTextField;

    private Map<String,String> map;
    @FXML
    public void handleBuy() {
        // Retrieving data from the fields.
        String clientName=clientNameTextField.getText();
        String clientAddress=clientAddressTextField.getText();
        String touristsName=touristsNameTextField.getText();
        String seats=seatsTextField.getText();

        // Inserting the data into a map.
        map=new HashMap<>();
        map.put("clientName", clientName);
        map.put("clientAddress", clientAddress);
        map.put("touristsName", touristsName);
        map.put("seats", seats);

        // Closing the dialogue.
        Stage stage = (Stage) this.clientNameTextField.getScene().getWindow();
        stage.close();
    }
    public Map<String, String> getData(){
        return map;
    }

}
