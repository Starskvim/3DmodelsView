package com.example.ModelView.services;

import com.example.ModelView.rest.exceptions.WebSyncGetException;
import com.example.ModelView.rest.exceptions.WebSyncPostException;
import com.example.ModelView.model.rest.PrintModelWeb;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static com.example.ModelView.utillity.Constant.Log.CREATE_POST;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebRestService {

    @Value("${webApp.urlPostWeb}")
    private String urlPostWebApp;

    @Value("${webApp.urlGetWeb}")
    private String urlGetWebApp;

    private RestTemplate restTemplate;

    public void createPostModel (PrintModelWeb printModelWeb){
        log.info(CREATE_POST, printModelWeb.getModelName(), urlPostWebApp);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<PrintModelWeb> entity = new HttpEntity<>(printModelWeb, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(urlPostWebApp,
                entity,
                String.class);
        if (response.getStatusCode() != HttpStatus.OK){
            throw new WebSyncPostException(printModelWeb.getModelName());
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
