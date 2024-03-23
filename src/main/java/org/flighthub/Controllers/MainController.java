package org.flighthub.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.flighthub.Domain.Agent;
import org.flighthub.Service.Service;
import org.flighthub.Repository.AgentRepository;

import java.util.UUID;

public class MainController {
    private Service service;
    private Agent agent;
    @FXML
    public Label usersName;

    public void setData(Service service, Agent agent) {
        this.service = service;
        this.agent = agent;
        initialize();
    }

    private void initialize() {
        usersName.setText(agent.getUsername());
    }

}