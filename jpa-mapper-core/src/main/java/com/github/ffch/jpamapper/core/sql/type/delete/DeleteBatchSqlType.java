package com.github.ffch.jpamapper.core.sql.type.delete;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.github.ffch.jpamapper.core.entity.JpaModelEntity;
import com.github.ffch.jpamapper.core.sql.helper.DefaultSqlHelper;
import com.github.ffch.jpamapper.core.sql.type.AbstractPrecisSqlType;
import com.github.ffch.jpamapper.core.sql.type.SqlType;

public class DeleteBatchSqlType extends AbstractPrecisSqlType {

	public static final DeleteBatchSqlType INSTANCE = new DeleteBatchSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.DELETE;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(DefaultSqlHelper.deleteSql());
		sql.append(DefaultSqlHelper.fromSql(jpaModelEntity));
		sql.append(DefaultSqlHelper.conditionIdsSql(jpaModelEntity, method));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}