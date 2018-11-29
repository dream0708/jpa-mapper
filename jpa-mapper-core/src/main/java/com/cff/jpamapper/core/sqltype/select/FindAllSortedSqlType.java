package com.cff.jpamapper.core.sqltype.select;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sql.PageAndSortSqlHelper;
import com.cff.jpamapper.core.sqltype.AbstractPageSortSqlType;

public class FindAllSortedSqlType extends AbstractPageSortSqlType {

	public static final FindAllSortedSqlType INSTANCE = new FindAllSortedSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makePageSortSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(PageAndSortSqlHelper.selectEntitySql(jpaModelEntity));
		sql.append(PageAndSortSqlHelper.fromSql(jpaModelEntity));
		sql.append(PageAndSortSqlHelper.sortSql(jpaModelEntity));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
