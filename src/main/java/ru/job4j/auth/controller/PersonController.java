package ru.job4j.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.auth.domain.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.dto.PersonPassDto;
import ru.job4j.auth.service.SimplePersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {
    private final SimplePersonService personService;
    private final ObjectMapper objectMapper;

    private void validPassword(Person person) {
        if (person.getPassword().length() < 5) {
            throw new IllegalArgumentException("Пароль указан не верно! Длина пароля не менее 6 символов");
        }
    }

    private void validPerson(Person person) {
        if (person.getLogin() == null || person.getPassword() == null) {
            throw new NullPointerException("Имя пользователя не должно быть пустым!");
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Person> signUp(@RequestBody Person person) {
        validPerson(person);
        validPassword(person);
        return this.personService.save(person)
                .map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Person>> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Job4jVladHeader", "job4j")
                .contentType(MediaType.APPLICATION_JSON)
                .body(personService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        return this.personService.findById(id)
                .map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Пользователь с указанным id не найден!"
                ));
    }

    @PutMapping("/")
    public ResponseEntity<Person> update(@RequestBody Person person) {
        validPerson(person);
        validPassword(person);
        if (this.personService.update(person)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        if (this.personService.delete(person)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void exceptionHandler(Exception e,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {{
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Person> patch(@PathVariable Integer id,
                                        @RequestBody PersonPassDto personPassDto) {
        return personService.patch(id, personPassDto)
                .map(result -> ResponseEntity.ok().body(result))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}