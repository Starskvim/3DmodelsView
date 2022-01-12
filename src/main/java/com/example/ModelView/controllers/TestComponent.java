package com.example.ModelView.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestComponent {
    private static Integer testInteger = 0;


    public static Integer getProgress(){
        testInteger ++;
        return testInteger;
    }
}
