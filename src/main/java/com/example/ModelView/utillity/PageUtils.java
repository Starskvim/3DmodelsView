package com.example.ModelView.utillity;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class PageUtils {

    public static List<Integer> preparePageIntService(int current, int totalPages) {
        List<Integer> pageNumbers = new ArrayList<>();
        int start = Math.max(current - 3, 0);
        int end = Math.min(totalPages, start + 9);
        pageNumbers.add(0);
        for (int i = start; i < end; i++) {
            if (i != 0 && i != totalPages - 1) {
                pageNumbers.add(i);
            }
        }
        pageNumbers.add(totalPages - 1);
        return pageNumbers;
    }

}
