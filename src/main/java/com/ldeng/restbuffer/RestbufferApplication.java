package com.ldeng.restbuffer;

import com.ldeng.restbuffer.Service.BufferService;
import com.ldeng.restbuffer.model.Person;
import com.ldeng.restbuffer.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.Date;

@SpringBootApplication
public class RestbufferApplication implements CommandLineRunner{

	Logger logger = LoggerFactory.getLogger(RestbufferApplication.class);

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private BufferService bufferService;

	public static void main(String[] args) {
		SpringApplication.run(RestbufferApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Person person = new Person();
		person.setName("john");

		if(personRepository.findByName("john")!=null) {
			logger.warn("User with name {} already exists.", "john");
		} else {
			personRepository.save(person);
		}

		bufferService.checkItems();
	}
}
