package com.cff.jpamapper.core.sql.type;

import java.lang.reflect.Method;

import com.cff.jpamapper.core.entity.JpaModelEntity;

public abstract class AbstractShardingSqlType implements SqlType {

	@Override
	public String makePageSortSql(JpaModelEntity jpaModelEntity, Method method) {
		return makeSql(jpaModelEntity, method);
	}
}
