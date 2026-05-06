package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import org.springframework.data.repository.CrudRepository;

/**
 * @author ccsw
 *
 */
public interface ClientRepository extends CrudRepository<Client, Long> {

    /**
     * Método para comprobar si ya existe un cliente con un nombre concreto.
     * Spring hace la consulta SQL por nosotros solo con leer el nombre del método.
     * * @param name Nombre del cliente
     * @return true si existe, false si no existe
     */
    boolean existsByName(String name);

}