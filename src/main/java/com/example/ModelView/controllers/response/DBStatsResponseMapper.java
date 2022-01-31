package com.example.ModelView.controllers.response;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBStatsResponseMapper implements RowMapper<DBStatsResponse> {

    static final double scale = Math.pow(10, 2);

    @Override
    public DBStatsResponse mapRow(ResultSet rs, int rowNum) throws SQLException {

        DBStatsResponse dbStatsResponse = new DBStatsResponse();

        dbStatsResponse.setTotalModels(rs.getInt("totalModels"));
        dbStatsResponse.setTotalOTH(rs.getInt("totalOTH"));
        dbStatsResponse.setTotalZIP(rs.getInt("totalZIP"));
        dbStatsResponse.setTotalSize(Math.round(rs.getDouble("totalSize") * scale) / scale);
        dbStatsResponse.setRatioAvg((int) Math.round(rs.getDouble("ratioAvg")));
        dbStatsResponse.setRatioMed(rs.getInt("ratioMed"));

        return dbStatsResponse;
    }
}
