在Java中，自带的几个函数式接口通常存放在java.util.function包中，常用的函数式接口包括：

1. **Consumer<T>**：表示接受一个输入参数并且不返回任何结果的操作。常用于对输入参数进行处理。

2. **Supplier<T>**：表示提供一个值（或者说是一个结果）的操作，没有参数输入。常用于延迟计算或者获取某个值。

3. **Function<T, R>**：表示接受一个输入参数，并返回一个结果的操作。常用于将输入映射为输出。

4. **Predicate<T>**：表示接受一个输入参数并返回一个布尔值的操作，常用于对输入进行条件判断。

5. **BiConsumer<T, U>**：表示接受两个输入参数并且不返回任何结果的操作。

6. **BiFunction<T, U, R>**：表示接受两个输入参数并返回一个结果的操作。

7. **UnaryOperator<T>**：表示接受一个参数并返回与输入参数类型相同的结果的操作。

8. **BinaryOperator<T>**：表示接受两个相同类型的参数并返回一个同类型的结果的操作。

9. **BiPredicate<T, U>**：表示接受两个输入参数并返回一个布尔值的操作。

这些函数式接口提供了一种方便的方式来定义和传递行为，可以通过lambda表达式或方法引用来实例化这些接口，从而实现函数式编程的特性。根据您的需求，选择合适的函数式接口来定义不同类型的操作。希望这个回答对您有所帮助！如果您有任何其他问题，请随时告诉我。

# 实例

以下是各个函数式接口的简要说明和示例用法：

1. **Consumer<T>**：接受一个输入参数并且不返回任何结果的操作。常用于对输入参数进行处理。

    ```java
    Consumer<String> consumer = (str) -> System.out.println("Hello, " + str);
    consumer.accept("World");
    ```

2. **Supplier<T>**：提供一个值（或者说是一个结果）的操作，没有参数输入。

    ```java
    Supplier<Double> supplier = () -> Math.random();
    double randomValue = supplier.get();
    ```

3. **Function<T, R>**：接受一个输入参数，并返回一个结果的操作。

    ```java
    Function<Integer, String> converter = (num) -> "Number: " + num;
    String result = converter.apply(100);
    ```

4. **Predicate<T>**：接受一个输入参数并返回一个布尔值的操作，常用于对输入进行条件判断。

    ```java
    Predicate<Integer> isPositive = (num) -> num > 0;
    boolean result = isPositive.test(5);
    ```

5. **BiConsumer<T, U>**：接受两个输入参数并且不返回任何结果的操作。

    ```java
    BiConsumer<String, Integer> biConsumer = (str, num) -> System.out.println(str + ": " + num);
    biConsumer.accept("Value", 10);
    ```

6. **BiFunction<T, U, R>**：接受两个输入参数并返回一个结果的操作。

    ```java
    BiFunction<Integer, Integer, Integer> adder = (a, b) -> a + b;
    int sum = adder.apply(3, 5);
    ```

7. **UnaryOperator<T>**：接受一个参数并返回与输入参数类型相同的结果的操作。

    ```java
    UnaryOperator<Integer> square = (num) -> num * num;
    int result = square.apply(5);
    ```

8. **BinaryOperator<T>**：接受两个相同类型的参数并返回一个同类型的结果的操作。

    ```java
    BinaryOperator<Integer> multiplier = (a, b) -> a * b;
    int product = multiplier.apply(3, 4);
    ```

9. **BiPredicate<T, U>**：接受两个输入参数并返回一个布尔值的操作。

    ```java
    BiPredicate<Integer, Integer> isEqual = (num1, num2) -> num1.equals(num2);
    boolean result = isEqual.test(10, 10);
    ```

通过使用这些函数式接口，您可以更方便地定义和传递行为，实现更灵活的编程方式。根据具体需求，选择合适的函数式接口来完成不同类型的操作。希望以上信息对您有所帮助！如果您有任何其他问题，请随时告诉我。
