package com.lind.uaa.keycloak.listener;

import com.alibaba.fastjson.JSON;
import com.lind.uaa.keycloak.cache.CacheProvider;
import com.lind.uaa.keycloak.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.Optional;

/**
 * kc-kafka默认不开启，只能配置kc-kafka.enabled为true时，才开启这个功能
 *
 * @author lind
 * @date 2023/3/1 11:32
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(value = "keycloak.uaa.kafka.enabled", matchIfMissing = true)
public class RoleChangeListener {

	private final CacheProvider cacheProvider;

	void handler(ConsumerRecord<String, String> record, Acknowledgment ack) {
		Optional<ConsumerRecord<String, String>> message = Optional.ofNullable(record);
		if (message.isPresent()) {
			int errorRetry = 3;
			int retry = 0;
			Exception latestException = null;
			while (++retry < errorRetry) {
				try {
					String s = message.get().value();
					com.alibaba.fastjson.JSONObject jsonObj = JSON.parseObject(s);
					String userId = null;
					switch (record.topic()) {
					case "KC_REALM_ROLE_MAPPING_CREATE":
						// KC_REALM_ROLE_MAPPING_CREATE在kc发布事件时对这个authDetails.userId进行了重写，默认它是操作人，现在改为被操作人了
						userId = jsonObj.getJSONObject("authDetails").getString("userId");
						break;
					case "KC_REALM_ROLE_MAPPING_DELETE":
						userId = jsonObj.getString("resourcePath").split("/")[1];
						break;
					default:
						break;
					}
					if (userId != null) {
						System.err.println("用户【" + userId + "】被更新了角色");
						cacheProvider.add(TokenUtil.NEED_REFRESH_TOKEN + ":" + userId, "1");
					}
					ack.acknowledge();
					return;
				}
				catch (Exception e) {
					latestException = e;
					try {
						Thread.sleep(100 * retry);
					}
					catch (InterruptedException interruptedException) {
					}
				}
			}
			ack.acknowledge();
			log.error("kafka消费失败", latestException);
		}
	}

	@KafkaListener(topics = { "KC_REALM_ROLE_MAPPING_CREATE", "KC_REALM_ROLE_MAPPING_DELETE" },
			groupId = "${spring.application.name}", containerFactory = "manulKafkaListenerContainerFactory")
	public void roleChangeCreate(ConsumerRecord<String, String> record, Acknowledgment ack) {
		handler(record, ack);
	}

}
