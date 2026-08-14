package model.controllers;

import model.entities.Client;
import model.services.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<Client> insert(@RequestBody Client client){
        client = clientService.registerClient(client);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{clientId}").buildAndExpand(client.getId()).toUri();
        return ResponseEntity.created(uri).body(client);
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<Client> update(@PathVariable Long clientId, @RequestBody Client client){
        client = clientService.updateClient(clientId, client);
        return ResponseEntity.ok().body(client);
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> delete(@PathVariable Long clientId){
        clientService.removeClient(clientId);
        return ResponseEntity.noContent().build();
    }

}
