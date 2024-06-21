package ru.job4j.auth.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.job4j.auth.domain.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.service.SimplePersonService;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final SimplePersonService personService;

    public PersonController(SimplePersonService personService, BCryptPasswordEncoder encoder) {
        this.personService = personService;

    }

    @PostMapping("/sign-up")
    public ResponseEntity<Person> signUp(@RequestBody Person person) {
        return this.personService.save(person)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @GetMapping("/all")
    public List<Person> findAll() {
        return this.personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = this.personService.findById(id);
        return new ResponseEntity<Person>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        return this.personService.update(person) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        return this.personService.delete(person) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
