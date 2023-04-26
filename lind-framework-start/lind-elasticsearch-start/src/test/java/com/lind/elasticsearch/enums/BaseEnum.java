package com.lind.elasticsearch.enums;

public interface BaseEnum<E extends Enum<?>, T> {

	Integer getValue();

	String getDescription();

}