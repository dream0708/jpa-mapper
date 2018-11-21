package com.cff.jpamapper.core.sqltype.select;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.exception.JpaMapperException;
import com.cff.jpamapper.core.method.MethodTypeHelper;
import com.cff.jpamapper.core.sql.JpaMapperSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;
import com.cff.jpamapper.core.util.StringUtil;

public class FindBySqlType implements SqlType {

	public static final FindBySqlType INSTANCE = new FindBySqlType();

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
		
		String name = method.getName();
		String para = name.replaceFirst(MethodTypeHelper.SELECT, "");
		if (StringUtil.isEmpty(para)) {
			throw new JpaMapperException("findBy条件不完整！");
		}
		String params[] = para.split(CONDITION_AND);
		if (params == null || params.length < 1) {
			throw new JpaMapperException("findBy条件不完整！");
		}
		
		sql.append(JpaMapperSqlHelper.conditionRegBySql(jpaModelEntity, params));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}