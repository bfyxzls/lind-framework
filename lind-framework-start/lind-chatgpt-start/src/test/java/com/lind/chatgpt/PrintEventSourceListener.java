package com.lind.chatgpt;

import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

/**
 * @author lind
 * @date 2023/4/20 10:26
 * @since 1.0.0
 */
@Slf4j
public class PrintEventSourceListener extends EventSourceListener {

	public void onEvent(EventSource eventSource, String id, String type, String data) {
		log.info("OpenAI：{}", data);
		if (data.equals("[DONE]")) {
			log.info("OpenAI Finish!");
		}
	}

}
