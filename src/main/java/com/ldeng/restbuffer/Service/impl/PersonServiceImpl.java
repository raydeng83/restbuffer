package com.ldeng.restbuffer.Service.impl;

import com.ldeng.restbuffer.RestbufferApplication;
import com.ldeng.restbuffer.Service.PersonService;
import com.ldeng.restbuffer.model.Person;
import com.ldeng.restbuffer.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService{


    @Autowired
    private PersonRepository personRepository;

    @Override
    public Person getPersonByName(String name) {
        Person person = personRepository.findByName(name);

        return person;
    }
}
