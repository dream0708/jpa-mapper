package com.cff.jpamapper.core.sqltype.update;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sql.JpaMapperSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;

public class UpdateSqlType implements SqlType {

	public static final UpdateSqlType INSTANCE = new UpdateSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.UPDATE;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.updateSql(jpaModelEntity));
		sql.append(JpaMapperSqlHelper.setSql(jpaModelEntity));
		sql.append(" </script>");
		return sql.toString().trim();
	}

}
