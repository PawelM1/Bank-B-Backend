package com.pab.bankb.controller;

import com.pab.bankb.dto.ClientDto;
import com.pab.bankb.model.Client;
import com.pab.bankb.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/client")
public class ClientController {

    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<Void> addNewClientAndCreateNewAccount(@RequestBody Client client) {
        clientService.addNewClientAndCreateNewAccount(client);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/all-client")
    public ResponseEntity<List<ClientDto>> getAllClient() {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getAllClientList());
    }
}
