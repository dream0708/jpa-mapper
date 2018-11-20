package com.cff.jpamapper.core.sqltype.insert;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sql.JpaMapperSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;

public class SaveAllSqlType implements SqlType {

	public static final SaveAllSqlType INSTANCE = new SaveAllSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.INSERT;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.insertSql(jpaModelEntity));
		sql.append(JpaMapperSqlHelper.valuesCollectionSql(jpaModelEntity, false));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
