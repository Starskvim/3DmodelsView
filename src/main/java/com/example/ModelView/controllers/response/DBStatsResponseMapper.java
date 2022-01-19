package com.example.ModelView.controllers.response;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBStatsResponseMapper implements RowMapper<DBStatsResponse> {

    @Override
    public DBStatsResponse mapRow(ResultSet rs, int rowNum) throws SQLException {

        DBStatsResponse dbStatsResponse = new DBStatsResponse();
        dbStatsResponse.setTotalModels(rs.getInt("totalModels"));
        dbStatsResponse.setTotalOTH(rs.getInt("totalOTH"));
        dbStatsResponse.setTotalZIP(rs.getInt("totalZIP"));
        dbStatsResponse.setTotalSize(rs.getDouble("totalSize"));

        return dbStatsResponse;
    }
}
