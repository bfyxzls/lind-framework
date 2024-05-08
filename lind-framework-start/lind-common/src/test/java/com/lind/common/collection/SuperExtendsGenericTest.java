package com.lind.common.collection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author lind
 * @date 2024/2/21 10:12
 * @since 1.0.0
 */
public class SuperExtendsGenericTest {

	/**
	 * 通配符的上限 可接收Integer和它的子类
	 * @param data
	 */
	public static void printNum(List<? extends Number> data) {
		for (Number number : data) {
			System.out.println(number);
		}
	}

	// JDK的Collections.copy方法是一个非常好的的例子，它实现了把源列表中的所有元素拷贝到目标列表中对应的索引位置上
	// 源列表是用来提供数据的，所以src变量需要限定上界，带有extends关键字，目标列表是用来写入数据的，所以dest变量需要界定上界，带有super关键字
	public static <T> void copy(List<? super T> dest, List<? extends T> src) {
		for (int i = 0; i < src.size(); i++)
			dest.set(i, src.get(i));
	}

	@Test
	public void testExtends() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		printNum(list);
		List<Long> list2 = Arrays.asList(1L, 2L, 3L);
		printNum(list2);
	}

	@Test
	public void testSuper() {
		JwtToken jwtToken = new JwtToken();
		jwtToken.setUserId("1");
		jwtToken.setRoles("admin");
		jwtToken.setJti("123");
		jwtToken.verify(i -> i.getUserId().equals("1"), i -> i.getRoles().equals("admin"), i -> i.getJti() != null);

		Md5Token md5Token = new Md5Token();
		md5Token.setJti("abc123");
		md5Token.verify(i -> i.getJti().equals("abc123"));
	}

	@Test
	public void testR() {
		R<Integer> result = new R<>();
		result.setCode(200);
		result.setData(1024);
		new RetOps<>(result).assertCode(200, i -> new RuntimeException("error " + i.getCode()))
				.assertDataNotNull(r -> new IllegalStateException("oops!")).useData(System.out::println);

	}

	static class RetOps<T> {

		R<T> original;

		RetOps(R<T> original) {
			this.original = original;
		}

		public static <T> RetOps<T> of(R<T> original) {
			return new RetOps<>(Objects.requireNonNull(original));
		}

		/**
		 * 断言业务数据有值
		 * @param func 用户函数,负责创建异常对象
		 * @param <Ex> 异常类型
		 * @return 返回实例，以便于继续进行链式操作
		 * @throws Ex 断言失败时抛出
		 */
		public <Ex extends Exception> RetOps<T> assertDataNotNull(Function<? super R<T>, ? extends Ex> func) throws Ex {
			if (Objects.isNull(original)) {
				throw func.apply(original);
			}
			return this;
		}

		/**
		 * 断言{@code code}的值
		 * @param expect 预期的值
		 * @param func 用户函数,负责创建异常对象
		 * @param <Ex> 异常类型
		 * @return 返回实例，以便于继续进行链式操作
		 * @throws Ex 断言失败时抛出
		 */
		public <Ex extends Exception> RetOps<T> assertCode(Integer expect, Function<? super R<T>, ? extends Ex> func)
				throws Ex {
			/**
			 * 对方法名的解释 这种方法签名设计在Java中主要用于实现异常处理和函数式编程的结合。让我们逐步解释这个方法签名的作用和含义：
			 *
			 * 1. `<Ex extends Exception>`：这是泛型声明，表示该方法可以抛出某种类型的异常。`<Ex extends
			 * Exception>`表明`Ex`是一个继承自`Exception`的异常类型。
			 *
			 * 2. `RetOps<T>`：这是方法的返回类型，表示该方法会返回一个`RetOps`类型的对象。
			 *
			 * 3. `assertSuccess`：这是方法的名称，表示这是一个用于断言成功的方法。
			 *
			 * 4. `Function<? super R<T>, ? extends Ex>
			 * func`：这是方法的参数，它接受一个函数作为输入。该函数接受一个`R<T>`类型的参数（或其超类），并返回一个`Ex`类型的异常（或其子类）。这里使用了通配符`?
			 * super R<T>`和`? extends Ex`，表示对参数类型有一定的限制，但具体类型在调用时确定。
			 *
			 * 5. `throws Ex`：表示该方法可能会抛出`Ex`类型的异常。
			 *
			 * 在这个方法中，通过泛型和函数式接口的设计，实现了灵活的异常处理机制。调用者可以传入一个函数，该函数根据输入的`R<T>`类型的参数（或其超类）来决定是否抛出某种类型的异常，从而实现更加灵活的异常处理逻辑。
			 *
			 * 另外，你提到了`super`关键字，在泛型中的`? super
			 * T`表示"任何类型，但必须是T的父类"，这样的设定使得参数类型更加灵活，可以接受T的父类作为参数，增强了方法的通用性和适用性。
			 *
			 */
			if (!original.getCode().equals(expect)) {
				throw func.apply(original);
			}
			return this;
		}

		/**
		 * 消费数据,注意此方法保证数据可用
		 * @param consumer 消费函数
		 */
		public void useData(Consumer<? super T> consumer) {
			consumer.accept(original.getData());
		}

	}

	class RBase<T> {

		private String id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}

	class R<T> extends RBase<T> {

		T data;

		Integer code;

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}

	}

	abstract class Token {

		private String jti;

		public String getJti() {
			return jti;
		}

		public void setJti(String jti) {
			this.jti = jti;
		}

	}

	class IdToken extends Token {

		private String userId;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

	}

	class JwtToken extends IdToken {

		private String roles;

		public String getRoles() {
			return roles;
		}

		public void setRoles(String roles) {
			this.roles = roles;
		}

		/**
		 * JwtToken类校验方法 通配符的下限,校验实体字段的方法，通过传入实体的Predicate条件，来对当前实体进行校验
		 * @return
		 */
		public void verify(Predicate<? super JwtToken>... checks) {
			for (Predicate<? super JwtToken> check : checks) {
				if (!check.test(this)) {
					throw new IllegalArgumentException("JWT token check failed for check " + check);
				}
			}
		}

	}

	class Md5Token extends Token {

		public void verify(Predicate<? super Md5Token>... checks) {
			for (Predicate<? super Md5Token> check : checks) {
				if (!check.test(this)) {
					throw new IllegalArgumentException("Md5 token check failed for check " + check);
				}
			}
		}

	}

}
