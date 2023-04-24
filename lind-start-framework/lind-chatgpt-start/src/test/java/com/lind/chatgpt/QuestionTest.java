package com.lind.chatgpt;

import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.common.Choice;
import com.unfbx.chatgpt.entity.completions.Completion;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

/**
 * @author lind
 * @date 2023/4/19 14:06
 * @since 1.0.0
 */
public class QuestionTest {

	private OpenAiStreamClient client;

	@Test
	public void test() {
		String question = "我想用java写一个二叉树遍历";
		for (Choice choice : GptUtil.getAnswer(question)) {
			System.out.println(choice.getText());
		}
	}

	@Test
	public void chat() {
		String question = "hello";
		for (ChatChoice choice : GptUtil.getChat(question)) {
			System.out.println(choice.getMessage());
		}
	}

	@Before
	public void before() {
		// 创建流式输出客户端
		client = OpenAiStreamClient.builder().connectTimeout(50).readTimeout(50).writeTimeout(50)
				.apiKey(GptUtil.GPT_KEY).proxy(GptUtil.getProxy()).build();
	}

	// GPT-3.5-Turbo模型
	@Test
	public void chatCompletions() {
		PrintEventSourceListener eventSourceListener = new PrintEventSourceListener();
		Message message = Message.builder().role(Message.Role.USER).content("我想用java写一个二叉树遍历").build();
		ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message)).build();
		client.streamChatCompletion(chatCompletion, eventSourceListener);
		CountDownLatch countDownLatch = new CountDownLatch(1);
		try {
			countDownLatch.await();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// 常规对话模型
	@Test
	public void completions() {
		// ConsoleEventSourceListener eventSourceListener = new
		// ConsoleEventSourceListener();
		PrintEventSourceListener eventSourceListener = new PrintEventSourceListener();
		Completion q = Completion.builder().prompt("我想求职java工作师，请帮我写一个简历").stream(true).build();
		client.streamCompletions(q, eventSourceListener);
		CountDownLatch countDownLatch = new CountDownLatch(1);// 线程计数器，线程完成后countDownLatch.countDown()，计数器减一，当计数器为0时，主线程继续执行
		try {
			countDownLatch.await();// 主线程等待
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
