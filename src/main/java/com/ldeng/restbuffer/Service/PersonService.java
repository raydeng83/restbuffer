package com.ldeng.restbuffer.Service;

import com.ldeng.restbuffer.model.Person;

public interface PersonService {

    Person getPersonByName(String name);

    Person savePerson(Person person);
}
