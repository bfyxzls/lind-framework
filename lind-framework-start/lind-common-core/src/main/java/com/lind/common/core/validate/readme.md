# 实体检验
* 通过注解实现实体检验，定义@interface注释
* 实现接口ConstraintValidator，绑定上面的注解
* 实现接口ConstraintValidator接口的方法isValid，实现实体检验逻辑

# 泛型通配符
## extend
> 使用方法可查看实例：com.lind.common.core.validate.flag.FlagValidator

List<? extends Number> 和 List<Number> 都是用来表示包含 Number 或 Number 的子类的列表，但它们在使用时有一些不同的场景。

1. **List<? extends Number>**：
    - 适用于当你希望接受包含 Number 及其子类的列表时，但不确定具体是哪个子类。
    - 例如，如果你希望可以接受 List<Integer>、List<Long> 等作为参数，可以使用 List<? extends Number>。
    - 这种方式更加灵活，因为它可以匹配任何继承自 Number 的类型。

2. **List<Number>**：
    - 适用于当你明确知道要接受的是 Number 类型的列表时使用。
    - 如果你确定只需要处理 Number 类型的列表，而不关心具体的子类，可以使用 List<Number>。
    - 这种方式更加明确，因为它只接受 Number 类型的列表，不包括其他子类。

总之，List<? extends Number> 更适合于需要处理多种 Number 子类的情况，而 List<Number> 更适合于明确知道要处理的是 Number 类型的情况。

## super
`List<? super T>`，它用于表示包含指定类型 T 或其父类的列表。下面是关于 `List<? super T>` 的使用场景：

1. **方法参数**：`List<? super T>` 可以用作方法参数，用来接受包含指定类型 T 或其父类的列表。

   ```java
   public void processList(List<? super Integer> list) {
       // 处理列表的逻辑
   }
   ```
在这个例子中，processList 方法可以接受包含 Integer 或其父类的列表，例如 List<Integer>、List<Number> 等。

2. **方法返回值**：`List<? super T>` 也可以用作方法的返回值，用来返回包含指定类型 T 或其父类的列表。

   ```java
   public List<? super Integer> createList() {
       List<Object> list = new ArrayList<>();
       list.add(1);
       return list;
   }
   ```
在这个例子中，createList 方法返回一个包含 Integer 或其父类的列表，例如 List<Integer>、List<Number> 等。

总之，`List<? super T>` 通配符用于表示包含指定类型 T 或其父类的列表，在方法参数和返回值中都能够提供更大的灵活性和通用性。
