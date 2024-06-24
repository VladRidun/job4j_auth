package ru.job4j.auth.controller;

import lombok.AllArgsConstructor;
import ru.job4j.auth.domain.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.service.SimplePersonService;

import java.util.List;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {
    private final SimplePersonService personService;

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
       return this.personService.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/")
    public ResponseEntity<Person> update(@RequestBody Person person) {
        if (this.personService.update(person)) {
            return  ResponseEntity.ok().build();
        }
        return  ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        if (this.personService.delete(person)) {
            return  ResponseEntity.ok().build();
        }
        return  ResponseEntity.notFound().build();
    }
}