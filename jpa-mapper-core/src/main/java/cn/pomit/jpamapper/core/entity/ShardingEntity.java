package cn.pomit.jpamapper.core.entity;


import cn.pomit.jpamapper.core.annotation.ShardingKey;

public class ShardingEntity {
	private String prefix = "";
	private String suffix = "";
	private String methodPrecis = "";
	private String methodRange = "";
	private String fieldName;
	private String fieldDeclaredName;
	private String entityFullName;
	
	public ShardingEntity(ShardingKey shardingKey, String fieldName, String fieldDeclaredName, String entityFullName){
		this.fieldName = fieldName;
		this.fieldDeclaredName = fieldDeclaredName;
		this.prefix = shardingKey.prefix();
		this.suffix = shardingKey.suffix();
		this.methodPrecis = shardingKey.methodPrecis();
		this.methodRange = shardingKey.methodRange();
		this.entityFullName = entityFullName;
	}
	
	public ShardingEntity() {
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldDeclaredName() {
		return fieldDeclaredName;
	}

	public void setFieldDeclaredName(String fieldDeclaredName) {
		this.fieldDeclaredName = fieldDeclaredName;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getMethodPrecis() {
		return methodPrecis;
	}

	public void setMethodPrecis(String methodPrecis) {
		this.methodPrecis = methodPrecis;
	}

	public String getMethodRange() {
		return methodRange;
	}

	public void setMethodRange(String methodRange) {
		this.methodRange = methodRange;
	}

	public String getEntityFullName() {
		return entityFullName;
	}

	public void setEntityFullName(String entityFullName) {
		this.entityFullName = entityFullName;
	}
}
