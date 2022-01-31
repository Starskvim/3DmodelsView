package com.example.ModelView.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DBStatsResponse {

    private Integer totalModels;

    private Integer totalOTH;

    private Integer totalZIP;

    private Double totalSize;

    private Integer ratioAvg;

    private Integer ratioMed;


}
