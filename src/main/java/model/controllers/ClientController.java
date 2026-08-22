package model.controllers;

import model.dtos.ClientCreateDTO;
import model.dtos.ClientUpdateDTO;
import model.entities.Client;
import model.entities.Vehicle;
import model.services.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/clients")
public class ClientController {
    private final ClientService clientService;
    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<Client>> findAll(){
        List<Client> clients = clientService.getClients();
        return ResponseEntity.ok().body(clients);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<Client> findById(@PathVariable Long clientId){
        Client client = clientService.findClientById(clientId);
        return ResponseEntity.ok().body(client);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Client> findByCpf(@PathVariable String cpf){
        Client client = clientService.findClientByCpf(cpf);
        return  ResponseEntity.ok().body(client);
    }


    @PostMapping
    public ResponseEntity<Client> insert(@RequestBody ClientCreateDTO clientCreateDTO){
        Client saveClient = clientService.registerClient(clientCreateDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{clientId}").buildAndExpand(saveClient.getId()).toUri();
        return ResponseEntity.created(uri).body(saveClient);
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<Client> update(@PathVariable Long clientId, @RequestBody ClientUpdateDTO clientUpdateDTO){
        Client updateClient = clientService.updateClient(clientId, clientUpdateDTO);
        return ResponseEntity.ok().body(updateClient);
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> delete(@PathVariable Long clientId){
        clientService.removeClient(clientId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{clientId}/vehicles")
    public ResponseEntity<Collection<Vehicle>> getVehiclesByClient(@PathVariable Long clientId){
        Collection<Vehicle> vehicles = clientService.getVehiclesByClient(clientId);
        return ResponseEntity.ok().body(vehicles);
    }

}
