package com.lind.kafka.producer;

import com.lind.kafka.entity.MessageEntity;
import com.lind.kafka.handler.FailureHandler;
import com.lind.kafka.handler.SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Map;

@RequiredArgsConstructor
public class ListenableFutureCallbackWithTracing<T extends SendResult<String, String>>
		implements ListenableFutureCallback<T> {

	protected final Map<String, String> mdcContextMap;

	private final SuccessHandler successHandler;

	private final FailureHandler failureHandler;

	private final String topic;

	private final MessageEntity message;

	@Override
	@SuppressWarnings("deprecation")
	public void onSuccess(T result) {

		if (successHandler != null) {
			successHandler.onSuccess(result, mdcContextMap);
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public void onFailure(Throwable ex) {
		if (failureHandler != null) {
			failureHandler.onFailure(topic, message, ex, mdcContextMap);
		}
	}

}
