package com.example.ModelView.rest.response;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBStatsResponseMapper implements RowMapper<DataBaseStatsView> {

    static final double scale = Math.pow(10, 2);

    @Override
    public DataBaseStatsView mapRow(ResultSet rs, int rowNum) throws SQLException {

        DataBaseStatsView dataBaseStatsView = new DataBaseStatsView();

        dataBaseStatsView.setTotalModels(rs.getInt("totalModels"));
        dataBaseStatsView.setTotalOTH(rs.getInt("totalOTH"));
        dataBaseStatsView.setTotalZIP(rs.getInt("totalZIP"));
        dataBaseStatsView.setTotalSize(Math.round(rs.getDouble("totalSize") * scale) / scale);
        dataBaseStatsView.setRatioAvg((int) Math.round(rs.getDouble("ratioAvg")));
        dataBaseStatsView.setRatioMed(rs.getInt("ratioMed"));

        return dataBaseStatsView;
    }
}
