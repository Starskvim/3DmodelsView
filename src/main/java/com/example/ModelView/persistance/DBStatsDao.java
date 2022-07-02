package com.example.ModelView.persistance;


import com.example.ModelView.rest.response.DBStatsResponse;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public interface DBStatsDao {

    void setDataSource(DataSource dataSource);

    DBStatsResponse getStats ();

}
