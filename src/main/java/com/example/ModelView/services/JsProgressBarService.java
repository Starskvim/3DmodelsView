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

    public static void setCurrentCount(Integer count){
        double count2 = (double)count;
        double totalCount2 = (double) totalCount;
        currentCount = (int) Math.round(count2 / totalCount2 * 100.0);
    }

    public static void setTotalCount(int total){
        totalCount = total;
    }

    public static synchronized void setCurrentTask (String task){
        currentTask = task;
    }

    public static Integer getCurrentCount(){
        return currentCount;
    }

    public static String getCurrentTask(){
        return currentTask;
    }

}
