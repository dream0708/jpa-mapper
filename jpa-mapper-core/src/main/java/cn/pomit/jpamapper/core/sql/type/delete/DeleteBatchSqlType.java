package cn.pomit.jpamapper.core.sql.type.delete;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.sql.helper.DefaultSqlHelper;
import cn.pomit.jpamapper.core.sql.type.AbstractPrecisSqlType;
import cn.pomit.jpamapper.core.sql.type.SqlType;

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
