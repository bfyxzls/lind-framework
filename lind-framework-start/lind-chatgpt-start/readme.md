# 使用前准备
* 需要申请openai的账号
* 需要生成一个openai的key
* 使用chatgtp-java的jar包，去调用openai的api
# 支持流式输出
官方对于解决请求缓慢的情况推荐使用流式输出模式。
主要是基于SSE 实现的（可以百度下这个技术）。也是最近在了解到SSE。OpenAI官网在接受Completions接口的时候，有提到过这个技术。 
Completion对象本身有一个stream属性，当stream为true时候Api的Response返回就会变成Http长链接。

