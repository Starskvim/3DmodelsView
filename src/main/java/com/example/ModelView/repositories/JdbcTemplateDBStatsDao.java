package com.example.ModelView.repositories;


import com.example.ModelView.controllers.response.DBStatsResponse;
import com.example.ModelView.controllers.response.DBStatsResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
@RequiredArgsConstructor
public class JdbcTemplateDBStatsDao implements DBStatsDao {

    private DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DBStatsResponse getStats() {

        String SQL = "SELECT (SELECT COUNT(*)FROM model_db.modelprint) AS totalModels,(SELECT COUNT(*)FROM model_db.model_zip_files) AS totalZIP,(SELECT COUNT(*)FROM model_db.model_other_files) AS totalOTH,(SELECT sum (model_zip_files.sizezip) / 1024 FROM model_db.model_zip_files) AS totalSize;";

        DBStatsResponse dbStatsResponse = jdbcTemplate.queryForObject(SQL, new DBStatsResponseMapper());

        return dbStatsResponse;
    }
}
