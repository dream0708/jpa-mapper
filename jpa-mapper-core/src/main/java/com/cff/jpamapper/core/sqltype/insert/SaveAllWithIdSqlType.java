package com.cff.jpamapper.core.sqltype.insert;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sql.DefaultSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;

public class SaveAllWithIdSqlType implements SqlType {

	public static final SaveAllWithIdSqlType INSTANCE = new SaveAllWithIdSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.INSERT;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(DefaultSqlHelper.insertSql(jpaModelEntity));
		sql.append(DefaultSqlHelper.valuesCollectionSql(jpaModelEntity, true));
		sql.append(" </script>");
		return sql.toString().trim();
	}

	@Override
	public String makeShardingSql(JpaModelEntity jpaModelEntity, Method method) {
		return null;
	}
}
