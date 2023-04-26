package com.lind.elasticsearch.audit;

import com.lind.elasticsearch.util.ClassHelper;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 拦截器 依赖于EsAuditorAwarer接口，开发人员通过实现这个接口将当前登陆人ID进行返回.
 */
@Component
@Aspect
public class AuditAspect {

	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired(required = false)
	EsAuditorAware esAuditorAware;

	/**
	 * 添加ES实体-切入点.
	 */
	@Pointcut("execution(* org.springframework.data.repository.CrudRepository.save(..))")
	public void save() {
	}

	/**
	 * 批量添加ES实体-切入点.
	 */
	@Pointcut("execution(* org.springframework.data.repository.CrudRepository.saveAll(..))")
	public void saveAll() {
	}

	/**
	 * 更新ES实体-切入点.
	 */
	@Pointcut("execution(* org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate.update(..))")
	public void update() {
	}

	@Before("saveAll()")
	public void beforeSaveAll(JoinPoint joinPoint) {
		if (joinPoint.getArgs().length > 0) {
			Object esBaseEntity = joinPoint.getArgs()[0];
			if (esBaseEntity instanceof Iterable) {
				System.out.println("save all aspect");
				Iterator it = ((Iterable) esBaseEntity).iterator();
				while (it.hasNext()) {
					saveAudit(it.next());
				}
			}
		}
	}

	/**
	 * 插入实体拦截器.
	 * @param joinPoint .
	 * @throws IllegalAccessException .
	 */
	@Before("save()")
	public void beforeSave(JoinPoint joinPoint) {
		System.out.println("save aspect");
		if (joinPoint.getArgs().length > 0) {
			Object esBaseEntity = joinPoint.getArgs()[0];
			saveAudit(esBaseEntity);
		}

	}

	@SneakyThrows
	void saveAudit(Object esBaseEntity) {
		Field[] fields = ClassHelper.getAllFields(esBaseEntity.getClass());
		List<Field> fieldList = Arrays.stream(fields)
				.filter(o -> o.isAnnotationPresent(CreatedDate.class) || o.isAnnotationPresent(LastModifiedDate.class))
				.collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(fieldList)) {
			for (Field field : fieldList) {
				field.setAccessible(true);// 取消私有字段限制
				if (field.get(esBaseEntity) == null) {
					field.set(esBaseEntity, df.format(new Date()));
				}
			}
		}
		List<Field> auditFieldList = Arrays.stream(fields)
				.filter(o -> o.isAnnotationPresent(CreatedBy.class) || o.isAnnotationPresent(LastModifiedBy.class))
				.collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(auditFieldList)) {
			for (Field field : auditFieldList) {
				field.setAccessible(true);// 取消私有字段限制
				if (field.get(esBaseEntity) == null && esAuditorAware != null) {
					field.set(esBaseEntity, esAuditorAware.getCurrentAuditor().orElse(null));
				}
			}
		}
	}

	/**
	 * 更新实体拦截器.
	 * @param joinPoint .
	 */
	@Before("update()")
	public void beforeUpdate(JoinPoint joinPoint) {
		System.out.println("update aspect");
		if (joinPoint.getArgs().length == 1 && joinPoint.getArgs()[0] instanceof UpdateQuery) {
			UpdateQuery updateQuery = (UpdateQuery) joinPoint.getArgs()[0];
			Map source = updateQuery.getDocument();
			Field[] fields = ClassHelper.getAllFields(updateQuery.getDocument().getClass());
			List<Field> fieldList = Arrays.stream(fields).filter(o -> o.isAnnotationPresent(LastModifiedDate.class))
					.collect(Collectors.toList());
			for (Field field : fieldList) {
				if (!source.containsKey(field.getName())) {
					source.put(field.getName(), df.format(new Date()));
				}
			}
			List<Field> auditFieldList = Arrays.stream(fields).filter(o -> o.isAnnotationPresent(LastModifiedBy.class))
					.collect(Collectors.toList());
			for (Field field : auditFieldList) {
				if (!source.containsKey(field.getName()) && esAuditorAware != null) {
					source.put(field.getName(), esAuditorAware.getCurrentAuditor().orElse(null));
				}
			}
			updateQuery.builder(updateQuery.getId()).withDocument(Document.from(source));
		}
	}

}
