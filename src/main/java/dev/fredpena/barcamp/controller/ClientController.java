package dev.fredpena.barcamp.controller;

import dev.fredpena.barcamp.data.tenant.entity.Person;
import dev.fredpena.barcamp.data.tenant.service.PersonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/persons")
@RequiredArgsConstructor
public class ClientController {

    private final PersonService personService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Person>> getPersons() {

        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping(path = "/{code}", produces = "application/json")
    public ResponseEntity<?> getPerson(@RequestParam @Valid @NotNull @Param("code") long code) {
        Optional<Person> person = personService.get(code);

        if (person.isEmpty()) {
            return (ResponseEntity<?>) ResponseEntity.notFound();
        }

        return ResponseEntity.ok(person.get());
    }
}