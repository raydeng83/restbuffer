package com.ldeng.restbuffer.Service;

import com.ldeng.restbuffer.model.Person;

import java.io.IOException;

public interface BufferService {
    void serviceCall(Person person) throws IOException;

    int createUserRest(Person person) throws IOException, InterruptedException;

    String createUser(Person person) throws IOException, InterruptedException;
}
