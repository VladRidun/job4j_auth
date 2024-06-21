package ru.job4j.auth.service;

import ru.job4j.auth.domain.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    List<Person> findAll();

    Optional<Person> findById(int id);

    Optional<Person> save(Person person);

    boolean update(Person person);

    boolean delete(Person person);
}
