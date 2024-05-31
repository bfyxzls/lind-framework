`javax.validation`和`jakarta.validation`都是用于Java中进行数据验证（validation）的相关API，它们提供了一套标准的验证框架，用于验证Java对象的属性是否符合指定的约束条件。这两个API的作用类似，只是在Java EE平台的演进过程中发生了一些变化。

### `javax.validation`：
- `javax.validation`是最初引入Java EE平台的数据验证API，定义在JSR 303（Bean Validation 1.0）和JSR 380（Bean Validation 2.0）中。
- 提供了注解（如@NotNull、@Size、@Email等）和验证器（Validator）等核心概念，用于对Java对象进行数据验证。
- 可以通过`Validator`接口来执行验证操作，检查对象是否符合预定义的约束。
- 在Java EE 6及之后的版本中，可以直接使用`javax.validation`进行数据验证。

### `jakarta.validation`：
- `jakarta.validation`是随着Java EE平台向Jakarta EE的迁移而产生的，为了避免因为商标问题而将原有的`javax`包迁移到`jakarta`包下。
- `jakarta.validation`与`javax.validation`具有相同的功能和API，但包名不同，属于Jakarta EE规范的一部分。
- 随着Java EE的发展，未来新的版本可能会使用`jakarta.validation`作为默认的数据验证API。

### 作用：
- **数据验证**：通过注解和验证器，对Java对象的属性进行验证，确保数据符合指定的约束条件。
- **提高数据质量**：有效地防止无效数据进入系统，提高数据的准确性和完整性。
- **简化开发**：通过标准化的验证框架，简化数据验证逻辑的编写和维护工作。

总的来说，`javax.validation`和`jakarta.validation`都是用于数据验证的标准API，可以帮助开发者实现对Java对象的数据验证功能，并提高系统的数据质量和稳定性。根据具体的Java平台版本和规范要求，选择合适的验证API来进行数据验证。

# 可能涉及到的包引用
jakarta
```
<dependency>
    <groupId>jakarta.validation</groupId>
    <artifactId>jakarta.validation-api</artifactId>
    <version>3.0.2</version>
</dependency>
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>7.0.5.Final</version>
</dependency>
```
javax
```
<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
    <version>1.1.0.Final</version>
</dependency>
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>5.4.3.Final</version>
</dependency>
```
> 请注意，当你手动引用hibernate-validator包时，是不需要再手动引用validation-api的，因为它已经被hibernate-validator依赖了

# 什么时候需要手动引用jakarta.validation-api

当你需要手动使用`Validation`对象时，这时，你必须手动显示的引入`jakarta.validation-api`，或者早期的`javax.validation:validation-api`包。

![](https://images.cnblogs.com/cnblogs_com/lori/2369799/o_240530013936_java-validate.png)

# 错误排除
当你使用7.0.5.Final版本的validator后，可能出现如何错误` dubbo rest business exception, error cause is: null
message is: Unable to create a Configuration, because no Jakarta Bean Validation provider could be found. Add a provider like Hibernate Validator (RI) to your classpath.`，这时，你需要升级你的版本，来解决这个错误，升级包如下：

 ```
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>8.0.1.Final</version>
</dependency>
 ```

升级到8.0.1.Final之后，问题得到解决，可正常提供错误消息

![](https://images.cnblogs.com/cnblogs_com/lori/2369799/o_240530015725_validate-phone.png)

验证工具类

 ```java
 /**
 * bean对象属性验证 使用此方法等同于参数前加注解@Validated,@Valid
 **/
public class BeanValidatorUtils {

	private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

	/**
	 * 校验实体中字段的合法性
	 * @param object
	 * @param groups
	 * @throws ConstraintViolationException
	 */
	public static void validateWithException(Object object, Class<?>... groups) throws ConstraintViolationException {
		Set<ConstraintViolation<Object>> constraintViolations = validatorFactory.getValidator().validate(object,
				groups);
		if (!constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(constraintViolations);
		}
	}

}

 ```
