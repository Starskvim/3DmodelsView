package com.example.ModelView.services;

import com.example.ModelView.controllers.exceptions.WebSyncGetException;
import com.example.ModelView.controllers.exceptions.WebSyncPostException;
import com.example.ModelView.dto.web.PrintModelWebDTO;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
@Setter
public class WebRestService {

    @Value("${webApp.urlPostWeb}")
    private String urlPostWebApp;

    @Value("${webApp.urlGetWeb}")
    private String urlGetWebApp;

    @Autowired
    private RestTemplate restTemplate;

    public void createPostModel (PrintModelWebDTO printModelWebDTO){

        System.out.println("create post - " + printModelWebDTO.getModelName());
        System.out.println("url " + urlPostWebApp);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<PrintModelWebDTO> entity = new HttpEntity<>(printModelWebDTO, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(urlPostWebApp, entity, String.class);

        if (response.getStatusCode() != HttpStatus.OK){
            throw new WebSyncPostException(printModelWebDTO.getModelName());
        }
    }

    public List<String> getWebModelList() {
        return this.exchangeAsList(urlGetWebApp, new ParameterizedTypeReference<List<String>>() {});
    }


    public <T> List<T> exchangeAsList(String uri, ParameterizedTypeReference<List<T>> responseType) {
        ResponseEntity<List<T>> response = restTemplate.exchange(uri, HttpMethod.GET, null, responseType);
        if(response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        } else {
            throw new WebSyncGetException();
        }
    }


}
