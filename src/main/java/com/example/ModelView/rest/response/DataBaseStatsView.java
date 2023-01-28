package com.example.ModelView.rest.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataBaseStatsView {

    private Integer totalModels;

    private Integer totalOTH;

    private Integer totalZIP;

    private Double totalSize;

    private Integer ratioAvg;

    private Integer ratioMed;
}
