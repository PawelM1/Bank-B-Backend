package com.pab.bankb.service;

import com.pab.bankb.dto.ClientDto;
import com.pab.bankb.model.Account;
import com.pab.bankb.model.Client;
import com.pab.bankb.repo.AccountRepo;
import com.pab.bankb.repo.ClientRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private ClientRepo clientRepo;
    private AccountRepo accountRepo;
    private AccountService accountService;

    public ClientService(ClientRepo clientRepo, AccountRepo accountRepo, AccountService accountService) {
        this.clientRepo = clientRepo;
        this.accountRepo = accountRepo;
        this.accountService = accountService;
    }

    @Transactional
    public void addNewClientAndCreateNewAccount(Client client) {
        if (clientRepo.existsByEmail(client.getEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client with given e-mail already exists");

        client = clientRepo.save(client);
        accountService.CreateAccount(client);
    }

    @Transactional
    public List<ClientDto> getAllClientList() {
        List<Client> clientList = clientRepo.findAll();
        List<ClientDto> clientDtoList = new ArrayList<>(100);

        for (Client client : clientList) {
            Optional<Account> account = accountRepo.findByClient(client);
            account.ifPresent(value -> clientDtoList.add(new ClientDto(client.getClientId(), client.getNameAndAddress(), client.getEmail(), client.getPhoneNumber(), value.getAccountNumber())));
        }

        return clientDtoList;
    }
}
