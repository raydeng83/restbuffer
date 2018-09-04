package com.ldeng.restbuffer.Service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ldeng.restbuffer.RestbufferApplication;
import com.ldeng.restbuffer.Service.BufferService;
import com.ldeng.restbuffer.Service.PersonService;
import com.ldeng.restbuffer.model.CreatePersonQueue;
import com.ldeng.restbuffer.model.FinishedCreationQueue;
import com.ldeng.restbuffer.model.Person;
import com.ldeng.restbuffer.repository.CreatePersonQueueRepository;
import com.ldeng.restbuffer.repository.FinishedCreationQueueRepository;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class BufferServiceImpl implements BufferService {

    Logger logger = LoggerFactory.getLogger(RestbufferApplication.class);

    @Autowired
    private PersonService personService;

    @Autowired
    private CreatePersonQueueRepository createPersonQueueRepository;

    @Autowired
    private FinishedCreationQueueRepository finishedCreationQueueRepository;


    public void serviceCall(Person person) throws IOException {
        String url = "http://localhost:8081/person";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(person);

//        String json = "{\"name\":\"" + person.getName() + "\"}";
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);

        logger.info(EntityUtils.toString(response.getEntity()));

        client.close();
    }


    @Override
    public int createUserRest(Person person) throws IOException, InterruptedException {
        String url = "http://localhost:8181/rest/api/2/user";

        String auth = "ray.deng83" + ":" + "Rochester24";
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.ISO_8859_1));
        String authHeader = "Basic " + new String(encodedAuth);
        boolean success = false;

        CloseableHttpClient client = HttpClients.createDefault();

        String body = null;
        int statusCode = 0;

        while (!success) {

            HttpPost httpPost = new HttpPost(url);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(person);

            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

            try {
                CloseableHttpResponse response = client.execute(httpPost);

                body = EntityUtils.toString(response.getEntity());

                statusCode = response.getStatusLine().getStatusCode();
                response.getStatusLine();

                int firstDigit = Integer.parseInt(Integer.toString(statusCode).substring(0, 1));

                success = (firstDigit == 2);

                if (!success) {
                    logger.info("Retry after 10 seconds...");
                    Thread.sleep(10000); // wait 2 seconds before retrying
                }
            } catch (Exception e) {
                logger.info("Connection error. Retry after 10 seconds...");
                Thread.sleep(10000); // wait 2 seconds before retrying
            }
        }


        client.close();

        logger.info(body);

        return statusCode;
    }

    @Override
    public String createUser(Person person) throws IOException, InterruptedException {
        person.setStatus("pending");
        person = personService.savePerson(person);
        CreatePersonQueue personQueue = new CreatePersonQueue();

        personQueue.setPerson(person);
        personQueue = createPersonQueueRepository.save(personQueue);


        int statusCode = createUserRest(person);

        logger.info("Status is: " + statusCode);

        int firstDigit = Integer.parseInt(Integer.toString(statusCode).substring(0, 1));

        if (firstDigit == 2) {
            logger.info("USer {} created successfully", person.getName());
            person.setStatus("created");
            FinishedCreationQueue finishedCreationQueue = new FinishedCreationQueue();
            finishedCreationQueue.setPerson(person);
            finishedCreationQueue = finishedCreationQueueRepository.save(finishedCreationQueue);

            createPersonQueueRepository.delete(personQueue);
        } else {
            logger.warn("User {} created failed. Will try again automatically later.", person.getName());
        }

        return "User " + person.getName() + " creation queued successfully";
    }

}
