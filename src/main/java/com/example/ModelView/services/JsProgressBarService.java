package com.example.ModelView.services;

import lombok.Getter;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Setter
@Getter
public class JsProgressBarService {
    private static volatile Integer currentCount = 0;

    private static volatile Integer totalCount = 0;

    private static volatile String currentTask = "Start Progress Bar";

    public static void setCurrentCount(int count){
        currentCount = (int) ((double)count / (double) totalCount) * 100;
    }

    public static void setTotalCount(int total){
        totalCount = total;
    }

    public static void setCurrentTask (String task){
        currentTask = task;
    }

    public static int getCurrentCount(){
        return currentCount;
    }

    public static String getCurrentTask(){
        return currentTask;
    }

}
