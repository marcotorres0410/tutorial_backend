package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Override
    public List<Client> findAll() {
        return (List<Client>) this.clientRepository.findAll();
    }

    @Override
    public void save(Long id, ClientDto dto) throws Exception {
        Client client;

        if (id == null) {
            // Es un ALTA NUEVA: Comprobamos directamente si el nombre ya existe
            if (this.clientRepository.existsByName(dto.getName())) {
                throw new Exception("El nombre del cliente ya existe.");
            }
            client = new Client();
        } else {
            // Es una MODIFICACIÓN: Recuperamos el cliente de BBDD
            client = this.clientRepository.findById(id).orElse(null);
            if (client == null) {
                throw new Exception("El cliente no existe.");
            }

            // Si le estamos intentando cambiar el nombre, comprobamos que el nuevo no esté pillado por otro
            if (!client.getName().equals(dto.getName()) && this.clientRepository.existsByName(dto.getName())) {
                throw new Exception("El nombre del cliente ya existe.");
            }
        }

        client.setName(dto.getName());
        this.clientRepository.save(client);
    }

    @Override
    public void delete(Long id) throws Exception {
        if (this.clientRepository.findById(id).orElse(null) == null) {
            throw new Exception("Not exists");
        }
        this.clientRepository.deleteById(id);
    }

    @Override
    public Client get(Long id) {
        return this.clientRepository.findById(id).orElse(null);
    }
}