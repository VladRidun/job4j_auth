package ru.job4j.auth.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
public class SimplePersonService implements PersonService, UserDetailsService {
    private PersonRepository persons;
    private BCryptPasswordEncoder encoder;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimplePersonService.class);

    @Override
    public List<Person> findAll() {
        return persons.findAll();
    }

    @Override
    public Optional<Person> findById(int id) {
        return persons.findById(id);
    }

    @Override
    public Optional<Person> save(Person person) {
        try {
            person.setPassword(encoder.encode(person.getPassword()));
            Person savedPerson = persons.save(person);
            return Optional.of(savedPerson);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    public boolean update(Person person) {
        var mayBePerson = persons.findById(person.getId());
        mayBePerson.ifPresent(p -> persons.save(p));
        return mayBePerson.isPresent();
    }

    @Override
    public boolean delete(Person person) {
        var mayBePerson = persons.findById(person.getId());
        mayBePerson.ifPresent(p -> persons.delete(p));
        return mayBePerson.isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = persons.findByLogin(username);
        if (person == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(person.getLogin(), person.getPassword(), emptyList());
    }
}
