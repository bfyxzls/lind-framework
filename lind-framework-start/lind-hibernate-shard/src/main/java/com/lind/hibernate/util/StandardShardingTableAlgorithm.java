package com.lind.hibernate.util;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @author lind
 * @date 2024/2/26 8:52
 * @since 1.0.0
 */
public class StandardShardingTableAlgorithm implements PreciseShardingAlgorithm<Long> {

	@Override
	public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
		long value = shardingValue.getValue();
		String suffix = value % 2 == 0 ? "0" : "1";
		for (String tableName : availableTargetNames) {
			if (tableName.endsWith(suffix)) {
				return tableName;
			}
		}
		throw new IllegalArgumentException();
	}

}
