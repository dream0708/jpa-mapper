package com.cff.jpamapper.core.sqltype.select;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sql.DefaultSqlHelper;
import com.cff.jpamapper.core.sqltype.AbstractPrecisSqlType;
import com.cff.jpamapper.core.sqltype.SqlType;

public class FindBatchSqlType extends AbstractPrecisSqlType {

	public static final FindBatchSqlType INSTANCE = new FindBatchSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(DefaultSqlHelper.selectEntitySql(jpaModelEntity));
		sql.append(DefaultSqlHelper.fromSql(jpaModelEntity));
		if (method.getParameterCount() > 0) {
			sql.append(DefaultSqlHelper.conditionIdsSql(jpaModelEntity, method));
		}
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
