package com.example.base.bo;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author benben
 * @date 2021-04-06 9:36
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes({List.class, PeopleBO.class})
public class PeopleTypeHandle extends BaseTypeHandler<List<PeopleBO>> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<PeopleBO> people, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JSON.toJSONString(people));
    }

    @Override
    public List<PeopleBO> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String json = resultSet.getString(s);
        return JSON.parseArray(json, PeopleBO.class);
    }

    @Override
    public List<PeopleBO> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String json = resultSet.getString(i);
        return JSON.parseArray(json, PeopleBO.class);
    }

    @Override
    public List<PeopleBO> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String json = callableStatement.getString(i);
        return JSON.parseArray(json, PeopleBO.class);
    }
}
