package com.cff.jpamapper.core.sqltype.select;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sql.JpaMapperSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;

public class FindBatchSqlType implements SqlType {

	public static final FindBatchSqlType INSTANCE = new FindBatchSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.selectEntitySql(jpaModelEntity));
		sql.append(JpaMapperSqlHelper.fromSql(jpaModelEntity));
		if (method.getParameterCount() > 0) {
			sql.append(JpaMapperSqlHelper.conditionIdsSql(jpaModelEntity, method));
		}
		sql.append(" </script>");
		return sql.toString().trim();
	}
}