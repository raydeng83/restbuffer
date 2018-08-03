package com.ldeng.restbuffer.repository;

import com.ldeng.restbuffer.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {
    Person findByName(String name);
}
