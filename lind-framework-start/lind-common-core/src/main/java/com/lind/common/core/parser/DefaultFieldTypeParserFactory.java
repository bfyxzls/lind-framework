package com.lind.common.core.parser;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class DefaultFieldTypeParserFactory implements FieldTypeParserFactory {

	private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

	private final Map<String, FieldTypeParser> parsers = new HashMap<>();

	@Override
	public FieldTypeParser getParser(Class<? extends FieldTypeParser> clazz) {

		reentrantReadWriteLock.readLock().lock();

		try {
			FieldTypeParser fieldTypeParser = parsers.get(clazz);
			if (Objects.nonNull(fieldTypeParser)) {
				return fieldTypeParser;
			}
		}
		finally {
			reentrantReadWriteLock.readLock().unlock();
		}
		reentrantReadWriteLock.writeLock().lock();
		try {
			FieldTypeParser parser = parsers.get(clazz);
			if (Objects.isNull(parser)) {
				parser = clazz.newInstance();
				parsers.put(parser.getClass().getName(), parser);
			}
			return parser;
		}
		catch (Exception e) {
			log.error("创建parser错误", e);
			throw new IllegalStateException("创建parser错误" + clazz);
		}
		finally {
			reentrantReadWriteLock.writeLock().unlock();
		}

	}

}
