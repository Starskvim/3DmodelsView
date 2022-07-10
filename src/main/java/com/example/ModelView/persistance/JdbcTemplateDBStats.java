package com.example.ModelView.persistance;


import com.example.ModelView.rest.response.DBStatsResponse;
import com.example.ModelView.rest.response.DBStatsResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
@RequiredArgsConstructor
public class JdbcTemplateDBStats implements DBStatsDao {

    private DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DBStatsResponse getStats() {

        String SQL = "SELECT (SELECT COUNT(*)FROM model_db.print_model) AS totalModels," +
                "(SELECT COUNT(*)FROM model_db.print_model_zip_data) AS totalZIP," +
                "(SELECT COUNT(*)FROM model_db.model_other_files) AS totalOTH," +
                "(SELECT sum (print_model_zip_data.zip_size) / 1024 FROM model_db.print_model_zip_data) AS totalSize," +
                "(SELECT avg (print_model_zip_data.archive_ratio) FROM model_db.print_model_zip_data) AS ratioAvg," +
                "(SELECT percentile_cont(0.5) WITHIN GROUP (ORDER BY archive_ratio) FROM model_db.print_model_zip_data) AS ratioMed;";

        DBStatsResponse dbStatsResponse = jdbcTemplate.queryForObject(SQL, new DBStatsResponseMapper());

        return dbStatsResponse;
    }
}
