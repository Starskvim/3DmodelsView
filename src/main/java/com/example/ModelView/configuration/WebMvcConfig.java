package com.example.ModelView.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;


import java.text.SimpleDateFormat;

//@Configuration
//public class WebMvcConfig {
//    @Bean
//    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        ObjectMapper mapper = converter.getObjectMapper();
//        Hibernate5Module hibernate5Module = new Hibernate5Module();
//        mapper.registerModule(hibernate5Module);
//        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//        return converter;
//    }
//}
