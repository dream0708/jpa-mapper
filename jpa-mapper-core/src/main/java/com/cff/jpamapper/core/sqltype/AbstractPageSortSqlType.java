package com.cff.jpamapper.core.sqltype;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;

public abstract class AbstractPageSortSqlType implements SqlType {
	
	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.UNKNOWN;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		return null;
	}

	@Override
	public String makeShardingSql(JpaModelEntity jpaModelEntity, Method method) {
		return null;
	}
}
