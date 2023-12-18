package com.lind.common.otp;

import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 时间区间测试.
 *
 * @author lind
 * @date 2023/12/14 15:11
 * @since 1.0.0
 */
@Slf4j
public class TimeBlockTest {

	Map<String, Long> config = ImmutableMap.of("second", 40L, "minute", 240L, "hour", 36000L);

	Map<String, Long> expiration = ImmutableMap.of("second", 1L, "minute", 60L, "hour", 3600L);

	Map<String, String> exp = new HashMap<>();

	String id = "global";

	@SneakyThrows
	@Test
	public void testSpan() {
		for (Map.Entry<String, Long> entry : expiration.entrySet()) {
			exp.put(entry.getKey(), "X-RateLimit-Limit-" + entry.getKey());
		}
		log.info("keys:{}", exp); // 缓存的key
		for (int i = 0; i < 120; i++) {
			Map<String, Usage> counters = new HashMap<>();
			for (Map.Entry<String, Long> entry : config.entrySet()) {
				Map<String, Long> ts = getTimestamps(LocalDateTime.now());
				counters.put(entry.getKey(),
						Usage.builder().currTime(ts.get(entry.getKey())).limit(config.get(entry.getKey())).build());

				log.info("{} window {}", entry.getKey(), ts.get("now") - (ts.get(entry.getKey())));
			}
			log.info("counts:{}", counters);
			TimeUnit.SECONDS.sleep(1);
		}

	}

	Map<String, Long> getTimestamps(LocalDateTime t) {
		int ye, mo, da;
		ye = t.getYear();
		mo = t.getMonthValue();
		da = t.getDayOfMonth();
		int ho, mi, se;
		ho = t.getHour();
		mi = t.getMinute();
		se = t.getSecond();
		Map<String, Long> ts = new HashMap<>();
		ts.put("now", t.atZone(ZoneId.systemDefault()).toEpochSecond());
		ts.put("second", LocalDateTime.of(ye, mo, da, ho, mi, se, 0).atZone(ZoneId.systemDefault()).toEpochSecond());
		ts.put("minute", LocalDateTime.of(ye, mo, da, ho, mi, 0, 0).atZone(ZoneId.systemDefault()).toEpochSecond());
		ts.put("hour", LocalDateTime.of(ye, mo, da, ho, 0, 0, 0).atZone(ZoneId.systemDefault()).toEpochSecond());
		return ts;
	}

}

@Data
@Builder
class Usage {

	private Long limit;

	private Long remaining;

	private Long usage;

	private Long currTime;

}
