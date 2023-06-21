# lock相关资源

---

## 当出现InterruptedException时，我们进行Thread.currentThread().interrupt();

当在一个线程中捕获到 `InterruptedException` 异常时，调用 `Thread.currentThread().interrupt()` 的作用是重新设置该线程的中断状态。

在 Java 中，线程的中断状态是一个标志位，用于指示当前线程是否被中断。当一个线程被中断时，它的中断状态会被设置为 `true`。通常情况下，当一个线程处于阻塞状态（如调用 `Thread.sleep()`、`Object.wait()` 或 `Lock.lock()` 等方法时），如果线程的中断状态被设置为 `true`，这些阻塞方法会立即抛出 `InterruptedException` 异常，以提前结束线程的阻塞状态。

当我们在捕获到 `InterruptedException` 异常时，我们可以选择如何处理这个异常。一种常见的做法是在异常处理代码中调用 `Thread.currentThread().interrupt()` 来重新设置线程的中断状态。这样做的目的是为了在后续的代码中传递中断状态，以便其他部分能够感知到线程的中断请求。

例如，以下是一个简单的示例：

```java
try {
    // 阻塞方法，可能抛出 InterruptedException
    Thread.sleep(1000);
} catch (InterruptedException e) {
    // 恢复中断状态
    Thread.currentThread().interrupt();
    // 其他异常处理逻辑
    // ...
}
```

在上述示例中，当 `InterruptedException` 被捕获时，我们首先调用 `Thread.currentThread().interrupt()` 来重新设置线程的中断状态，然后继续处理其他异常。通过重新设置中断状态，我们能够传递中断请求，并在后续代码中根据需要做出响应。

需要注意的是，调用 `Thread.currentThread().interrupt()` 只是设置了线程的中断状态，并不会真正中断线程的执行。线程是否真正中断还取决于后续的代码逻辑以及对中断状态的处理。通常情况下，在捕获到 `InterruptedException` 后，我们应该根据业务需要决定是否中断线程的执行，或者在适当的时候返回线程的执行结果。

总结起来，调用 `Thread.currentThread().interrupt()` 的作用是重新设置线程的中断状态，以便传递中断请求并在后续代码中进行响应。
