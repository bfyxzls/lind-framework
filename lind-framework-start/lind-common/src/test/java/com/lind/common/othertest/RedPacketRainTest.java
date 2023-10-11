package com.lind.common.othertest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lind
 * @date 2023/6/12 10:38
 * @since 1.0.0
 */
public class RedPacketRainTest {

	private static final int MAX_RETRY_COUNT = 3; // 最大重试次数

	public static List<Double> generateRedPackets(double totalAmount, int count) {
		List<Double> redPackets = new ArrayList<>();
		double remainingAmount = totalAmount;
		AtomicInteger remainingCount = new AtomicInteger(count);
		ConcurrentHashMap<String, Double> userRedPackets = new ConcurrentHashMap<>();
		Random random = new Random();

		while (remainingCount.get() > 0) {
			// 生成随机金额，范围在0到剩余金额之间
			double maxAmount = remainingAmount / remainingCount.get() * 2;
			double packetAmount = random.nextDouble() * maxAmount;
			// 保留两位小数
			packetAmount = Math.floor(packetAmount * 100) / 100;

			if (isValidAmount(packetAmount, remainingAmount, remainingCount.get())) {
				String userId = generateUserId();
				if (assignRedPacket(userId, packetAmount, userRedPackets)) {
					redPackets.add(packetAmount);
					remainingAmount -= packetAmount;
					remainingCount.decrementAndGet();
				}
			}
		}

		return redPackets;
	}

	private static boolean isValidAmount(double packetAmount, double remainingAmount, int remainingCount) {
		return packetAmount > 0 && (packetAmount * remainingCount <= remainingAmount);
	}

	private static String generateUserId() {
		// 实现生成唯一用户ID的逻辑，可以使用UUID或其他方法
		return UUID.randomUUID().toString();
	}

	private static boolean assignRedPacket(String userId, double packetAmount,
			ConcurrentHashMap<String, Double> userRedPackets) {
		int retryCount = 0;

		while (retryCount < MAX_RETRY_COUNT) {
			Double currentAmount = userRedPackets.putIfAbsent(userId, packetAmount);

			if (currentAmount == null) {
				// 成功分配红包给用户
				return true;
			}

			double newAmount = currentAmount + packetAmount;

			if (isValidAmount(newAmount, Double.MAX_VALUE, Integer.MAX_VALUE)) {
				if (userRedPackets.replace(userId, currentAmount, newAmount)) {
					// 成功更新用户的红包金额
					System.out.println("分给用户" + userId + "红包:" + newAmount);
					return true;
				}
			}

			retryCount++;
		}

		// 超过最大重试次数，分配失败
		return false;
	}

	public static void main(String[] args) {
		double totalAmount = 100.0;
		int count = 10;

		List<Double> redPackets = generateRedPackets(totalAmount, count);

		System.out.println("Generated red packets:");
		for (double amount : redPackets) {
			System.out.println(amount);
		}
	}

}
