package com.lind.chatgpt;

import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.common.Choice;
import com.unfbx.chatgpt.entity.completions.CompletionResponse;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.List;

/**
 * @author lind
 * @date 2023/4/19 14:05
 * @since 1.0.0
 */
public class GptUtil {

	public static final String GPT_KEY = "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";

	public static Proxy getProxy() {
		// 代理可以为null
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 10809));
		return proxy;
	}

	public static OpenAiClient getOpenAiClient() {
		// 代理可以为null
		OpenAiClient openAiClient = OpenAiClient.builder().apiKey(GPT_KEY).proxy(getProxy()).build();
		return openAiClient;
	}

	public static Choice[] getAnswer(String question) {
		OpenAiClient openAiClient = getOpenAiClient();
		CompletionResponse completions = openAiClient.completions(question);
		return completions.getChoices();
	}

	public static List<ChatChoice> getChat(String question) {
		OpenAiClient openAiClient = getOpenAiClient();
		Message message = Message.builder().role(Message.Role.USER).content("你好").build();
		ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message)).build();
		ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);
		return chatCompletionResponse.getChoices();
	}

}
