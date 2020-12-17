package com.pab.bankb.repo;

import com.pab.bankb.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepo extends JpaRepository<Client, Long> {

    Optional<Client> getClientByEmail(String email);

    Boolean existsByEmail(String email);
}
