# ForkJoinTask 对象有两种特定方法：
* fork() 方法允许计划 ForkJoinTask 异步执行。这允许从现有 ForkJoinTask 启动新的 ForkJoinTask。
* join() 方法允许 ForkJoinTask 等待另一个 ForkJoinTask 完成。
