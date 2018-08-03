package com.ldeng.restbuffer.Service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ldeng.restbuffer.RestbufferApplication;
import com.ldeng.restbuffer.Service.BufferService;
import com.ldeng.restbuffer.model.Person;
import org.apache.commons.codec.binary.Base64;
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
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class BufferServiceImpl implements BufferService{

    Logger logger = LoggerFactory.getLogger(RestbufferApplication.class);

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
    public String createUserRest(Person person) throws  IOException {
        String url = "http://localhost:5050/rest/api/2/user";

        String auth = "admin" + ":" + "password";
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.ISO_8859_1));
        String authHeader = "Basic " + new String(encodedAuth);


        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(person);

        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

        CloseableHttpResponse response = client.execute(httpPost);

        String body = EntityUtils.toString(response.getEntity());

        client.close();

        logger.info(body);

        return body;
    }

}
