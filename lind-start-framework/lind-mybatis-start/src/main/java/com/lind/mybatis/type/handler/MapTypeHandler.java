package com.lind.mybatis.type.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

/**
 * k/v字典map到字符串的转换.
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(Map.class)
public class MapTypeHandler extends BaseTypeHandler<Map> {

	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Map parameter, JdbcType jdbcType) throws SQLException {
		try {
			ps.setString(i, objectMapper.writeValueAsString(parameter));
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Map getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return Optional.ofNullable(rs.getString(columnName)).map(o -> {
			try {
				return objectMapper.readValue(o, Map.class);
			}
			catch (JsonProcessingException e) {
				e.printStackTrace();
				return null;
			}
		}).orElse(null);
	}

	@Override
	public Map getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return Optional.ofNullable(rs.getString(columnIndex)).map(o -> {
			try {
				return objectMapper.readValue(o, Map.class);
			}
			catch (JsonProcessingException e) {
				e.printStackTrace();
				return null;
			}
		}).orElse(null);
	}

	@Override
	public Map getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return Optional.ofNullable(cs.getString(columnIndex)).map(o -> {
			try {
				return objectMapper.readValue(o, Map.class);
			}
			catch (JsonProcessingException e) {
				e.printStackTrace();
				return null;
			}
		}).orElse(null);
	}

}
