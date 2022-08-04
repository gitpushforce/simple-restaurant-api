package com.simple.restaurant.serverapp.common;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Component
public class SQLCommon {

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Map<String, Object>> getListSQL(String sqlFileName, Map<String, Object> params) throws RuntimeException {
        String content = getSQL(sqlFileName);
        return jdbcTemplate.queryForList(content, params);
    }

    public Map<String, Object> getObjectSQL(String sqlFileName, Map<String, Object> params) throws RuntimeException {
        String content = getSQL(sqlFileName);
        try {
            return jdbcTemplate.queryForMap(content, params);
        } catch (Exception e) {
            return null;
        }
    }

    public int insUpdDelRow(String sqlFileName, Map<String, Object> params) throws RuntimeException {
        String content = getSQL(sqlFileName);
        return jdbcTemplate.update(content, params);
    }

    private String getSQL(String sqlFileName) throws RuntimeException {
        URL sqlUrl = Resources.getResource("sql/" + sqlFileName + ".sql");
        try {
            return Resources.toString(sqlUrl, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
