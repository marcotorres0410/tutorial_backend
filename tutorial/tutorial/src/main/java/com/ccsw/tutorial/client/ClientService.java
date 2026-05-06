package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;

import java.util.List;

/**
 * @author ccsw
 *
 */
public interface ClientService {

    List<Client> findAll();

    void save(Long id, ClientDto dto) throws Exception;

    void delete(Long id) throws Exception;

    Client get(Long id);
}