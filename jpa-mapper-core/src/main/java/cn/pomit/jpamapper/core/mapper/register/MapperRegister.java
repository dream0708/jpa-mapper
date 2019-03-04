package cn.pomit.jpamapper.core.mapper.register;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Table;

import org.apache.ibatis.session.Configuration;

import cn.pomit.jpamapper.core.annotation.Many;
import cn.pomit.jpamapper.core.annotation.One;
import cn.pomit.jpamapper.core.annotation.ShardingKey;
import cn.pomit.jpamapper.core.entity.JoinEntity;
import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.entity.ShardingEntity;
import cn.pomit.jpamapper.core.exception.JpaMapperException;
import cn.pomit.jpamapper.core.mapper.JMapper;
import cn.pomit.jpamapper.core.mapper.PagingAndSortingMapper;
import cn.pomit.jpamapper.core.mapper.SimpleShardingMapper;
import cn.pomit.jpamapper.core.mapper.builder.JpaMapperAnnotationBuilder;
import cn.pomit.jpamapper.core.util.ReflectUtil;
import cn.pomit.jpamapper.core.util.StringUtil;

public class MapperRegister {
	private Class<?> mapper;
	private List<Method> registerMethod = new ArrayList<>();
	private Configuration configuration;

	public static final int NO_MAPPER = -1;
	public static final int CRUD_MAPPER = 0;
	public static final int SHARDING_MAPPER = 2;
	public static final int PAGESORT_MAPPER = 1;
	
	int type = NO_MAPPER;

	public MapperRegister(Class<?> mapper, Configuration configuration) {
		this.mapper = mapper;
		this.configuration = configuration;
		scanMappers();
	}

	private void scanMappers() {
		Method[] methods = mapper.getMethods();
		for (Method method : methods) {
			if (method.getAnnotations() == null || method.getAnnotations().length < 1) {
				registerMethod.add(method);
			}
		}
	}

	public void genMappedStatement(String databaseName) {
		type = checkMapperType();
		if (type == NO_MAPPER)
			return;

		JpaMapperAnnotationBuilder jpaMapperAnnotationBuilder = new JpaMapperAnnotationBuilder(configuration, mapper);
		JpaModelEntity jpaModelEntity = parseModel();
		jpaModelEntity.setDatabaseName(databaseName);
		jpaMapperAnnotationBuilder.setJpaModelEntity(jpaModelEntity);
		for (Method method : registerMethod) {
			jpaMapperAnnotationBuilder.parseStatement(method);
		}
	}

	private int checkMapperType() {
		Class<?> interfases[] = mapper.getInterfaces();
		if (interfases == null || interfases.length < 1) {
			return NO_MAPPER;
		}
		for (Class<?> interfase : interfases) {
			if (ReflectUtil.checkTypeFit(interfase, JMapper.class)) {
				if (interfase.equals(SimpleShardingMapper.class)) {
					return SHARDING_MAPPER;
				} else if (interfase.equals(PagingAndSortingMapper.class)) {
					return PAGESORT_MAPPER;
				} else {
					return CRUD_MAPPER;
				}
			}
		}
		return NO_MAPPER;
	}

	private JpaModelEntity parseModel() {
		JpaModelEntity jpaModelEntity = new JpaModelEntity();
		if (type == SHARDING_MAPPER) {
			jpaModelEntity.setSharding(true);
		} else if (type == PAGESORT_MAPPER) {
			jpaModelEntity.setPageSort(true);
		}
		Class<?> entity = ReflectUtil.findGenericClass(mapper);
		if (entity == null) {
			throw new JpaMapperException("未能获取到Mapper的泛型类型");
		}
		Table tableAnnotation = entity.getAnnotation(Table.class);
		String tableName = entity.getSimpleName();
		jpaModelEntity.setTargertEntity(entity);
		if (tableAnnotation != null) {
			tableName = tableAnnotation.name();
		}
		jpaModelEntity.setTableName(tableName);

		Field fields[] = entity.getDeclaredFields();
		for (Field field : fields) {

			Id id = field.getAnnotation(Id.class);
			boolean isId = false;
			if (id != null)
				isId = true;

			// 联表注解
			if (field.getAnnotation(JoinColumns.class) != null || field.getAnnotation(JoinColumn.class) != null) {
				if (jpaModelEntity.getJoinEntity() != null)
					throw new JpaMapperException("JoinColumn(s)只能用一次哦！");
				JoinEntity joinEntity = new JoinEntity();

				One one = field.getAnnotation(One.class);
				Many many = field.getAnnotation(Many.class);
				if (one != null) {
					joinEntity.setMappingType(JoinEntity.ONE);
					joinEntity.setJoinType(one.type());
				} else if (many != null) {
					joinEntity.setMappingType(JoinEntity.MANY);
					joinEntity.setJoinType(many.type());
				} else {
					throw new JpaMapperException("JoinColumn(s)需要搭配cn.pomit.jpamapper.core.annotation.One(Many)一起使用哦！");
				}
				Map<String, String> joinColumns = new HashMap<>();
				JoinColumns joinColumnsAnno = field.getAnnotation(JoinColumns.class);
				if (joinColumnsAnno != null) {
					JoinColumn[] joinColumnArr = joinColumnsAnno.value();
					for (JoinColumn item : joinColumnArr) {
						joinColumns.put(item.name(), item.referencedColumnName());
					}
				}
				JoinColumn joinColumnAnno = field.getAnnotation(JoinColumn.class);
				if (joinColumnAnno != null) {
					joinColumns.put(joinColumnAnno.name(), joinColumnAnno.referencedColumnName());
				}
				if (joinColumns.size() > 0) {
					joinEntity.setJoinColumns(joinColumns);
				} else {
					throw new JpaMapperException("JoinColumn(s)字段不能为空！");
				}
				joinEntity.setEntityName(field.getName());
				joinEntity.setEntityType(field.getType());
				jpaModelEntity.setJoinEntity(joinEntity);
			}

			Column columnAnnotation = field.getAnnotation(Column.class);
			String fieldName = field.getName();
			String fieldDeclaredName = fieldName;
			if (columnAnnotation != null) {
				if (StringUtil.isNotEmpty(columnAnnotation.name())) {
					fieldDeclaredName = columnAnnotation.name();
				}
			} else {
				if (!isId)
					continue;
			}
			
			// 判断是否分表
			if (jpaModelEntity.isSharding()) {
				ShardingKey shardingKey = field.getAnnotation(ShardingKey.class);
				if (shardingKey != null) {
					String entityFullName = entity.getCanonicalName();
					ShardingEntity shardingEntity = new ShardingEntity(shardingKey, fieldName, fieldDeclaredName,
							entityFullName);
					jpaModelEntity.setShardingEntity(shardingEntity);
					continue;
				}
			}
			
			if (isId) {
				jpaModelEntity.setHasId(true);
				jpaModelEntity.setIdName(fieldName);
				jpaModelEntity.setIdColumn(fieldDeclaredName);
				jpaModelEntity.setIdField(field);
			} else {
				jpaModelEntity.addField(fieldName, fieldDeclaredName);
			}
		}
		return jpaModelEntity;
	}
}
