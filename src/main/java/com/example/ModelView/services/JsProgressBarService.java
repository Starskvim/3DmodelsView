package com.example.ModelView.services;

import lombok.Getter;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.ModelView.utillity.Constant.Log.START_PROGRESS_BAR;

@Service
@RequiredArgsConstructor
@Setter
@Getter
public class JsProgressBarService {

    private static volatile AtomicInteger currentCount = new AtomicInteger(0);

    private static volatile Integer totalCount = 0;

    private static volatile AtomicReference<String> currentTask = new AtomicReference<>(START_PROGRESS_BAR);

    public static void setCurrentCount(AtomicInteger count){
        double count2 = count.get();
        double totalCount2 = (double) totalCount;
        currentCount.set((int) Math.round(count2 / totalCount2 * 100.0));
    }

    public static void setTotalCount(int total){
        totalCount = total;
    }

    public static synchronized void setCurrentTask (String task){
        currentTask.set(task);
    }

    public static Integer getCurrentCount(){
        return currentCount.get();
    }

    public static String getCurrentTask(){
        return currentTask.get();
    }

}
