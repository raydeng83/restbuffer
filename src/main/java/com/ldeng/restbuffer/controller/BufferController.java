package com.ldeng.restbuffer.controller;

import com.ldeng.restbuffer.Service.BufferService;
import com.ldeng.restbuffer.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/buffer")
public class BufferController {

    @Autowired
    private BufferService bufferService;

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public String createUser(@RequestBody Person person) throws IOException {
        String responseString = bufferService.createUserRest(person);

        return responseString;
    }
}
