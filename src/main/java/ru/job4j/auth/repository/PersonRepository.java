package ru.job4j.auth.repository;

import ru.job4j.auth.domain.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Integer> {
}
