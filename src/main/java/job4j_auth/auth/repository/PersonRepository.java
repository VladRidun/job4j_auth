package job4j_auth.auth.repository;

import job4j_auth.auth.domain.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Integer> {
}
