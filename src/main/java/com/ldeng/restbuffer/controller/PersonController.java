package com.ldeng.restbuffer.controller;

import com.ldeng.restbuffer.Service.BufferService;
import com.ldeng.restbuffer.Service.PersonService;
import com.ldeng.restbuffer.model.Person;
import com.ldeng.restbuffer.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(("/person"))
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private BufferService bufferService;

    @RequestMapping("/{name}")
    public Person getPersonByName(@PathVariable String name) {
        Person person = personService.getPersonByName(name);

        return person;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Person createPerson(@RequestBody Person person) throws IOException {
        person = personRepository.save(person);

        bufferService.serviceCall(person);

        return person;
    }
}
