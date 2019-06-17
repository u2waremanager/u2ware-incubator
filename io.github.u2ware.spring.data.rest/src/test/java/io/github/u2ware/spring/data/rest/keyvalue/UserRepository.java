package io.github.u2ware.spring.data.rest.keyvalue;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    List<String> findByLastname(String lastname);
}