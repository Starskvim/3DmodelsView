package com.example.ModelView.repositories;


import com.example.ModelView.controllers.response.DBStatsResponse;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public interface DBStatsDao {

    void setDataSource(DataSource dataSource);

    DBStatsResponse getStats ();

}
