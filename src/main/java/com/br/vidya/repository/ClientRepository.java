package com.br.vidya.repository;

import com.br.vidya.model.Client;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClientRepository extends CreateOrUpdateRepository<Client, Long>, JpaSpecificationExecutor<Client> {
}
