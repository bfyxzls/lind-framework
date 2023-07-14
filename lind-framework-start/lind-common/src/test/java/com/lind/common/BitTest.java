package com.lind.common;

import com.lind.common.core.util.ByteUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 为什么使用右移操作符呢？
 *
 * 右移操作符在这里的好处是可以对整数进行快速的二进制位操作，避免了使用除法操作。除法运算相对较慢，尤其 * 是在某些硬件上。
 * 使用右移操作符可以提高计算效率，而且在容量计算上能够保持较好的整数近似值，避免了无用的浮点运算。
 */
@Slf4j
public class BitTest {

	private static final int COUNT_BITS = Integer.SIZE - 3;

	// 低29位的容量
	private static final int CAPACITY = (1 << COUNT_BITS) - 1;

	// 把runState理解为区间段，每个区间段都有独立的workCount，它们都是成对出现的
	private static final int RUNNING = -1 << COUNT_BITS;// -1*536,870,912

	private static final int SHUTDOWN = 0 << COUNT_BITS; // 0*536,870,912=0

	private static final int STOP = 1 << COUNT_BITS; // 1*536,870,912

	private static final int TIDYING = 2 << COUNT_BITS;// 2*536,870,912

	private static final int TERMINATED = 3 << COUNT_BITS;// 3*536,870,912

	private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

	// 获取行状态[高3位]
	// 简单来说，比如从100开始为状态，则第二个状态为100*2=200，而100到200之前有100个单元（0~99），每个状态之间都是100个空位，来存储（0~99）个数。
	private static int runStateOf(int c) {
		return c & ~CAPACITY;
	}

	// 获取工作数[低29位]
	private static int workerCountOf(int c) {
		return c & CAPACITY;
	}

	/**
	 * 状态和工作数生成的ctl,是一个数字，每个区间段通过runState来区分
	 * @param rs 工作数
	 * @param wc 状态值
	 * @return
	 */
	private static int ctlOf(int rs, int wc) {
		return rs | wc;
	}

	/**
	 * 一个数，存两个状态的值，高3位存runState,低29位保存workCount
	 */
	@Test
	public void runStateWorkCount() {
		// i = 10: 十进制 是10，二进制是 1010
		// i << 1: 左移1位，二进制变为 10100，转换位十进制 则是 20，相当于乘以2的1次数
		// i = 20: 十进制 是20，二进制是 10100
		// i >> 1: 右移1位，二进制变为 1010，转换位十进制 则是 10，相当于除以2的1次数
		// i << 3：相当于乘以2的3次方；i >> 3：相当于除以2的3次数
		System.out.println((1 << 3) - 1);
		System.out.println("CAPACITY=" + CAPACITY);
		System.out.println("COUNT_BITS=" + COUNT_BITS);
		System.out.println("RUNNING=" + RUNNING);
		System.out.println("SHUTDOWN=" + SHUTDOWN);
		System.out.println("STOP=" + STOP);
		System.out.println("TIDYING=" + TIDYING);
		System.out.println("TERMINATED=" + TERMINATED);

		// 阈值，将一个int32的数据分成了几个区间段，而这个状态值与递增数的临界点就是cap，如int32的低10位存递增，高22位存状态
		int cap = Integer.SIZE - 22;// 10
		cap = (1 << cap) - 1; // 向左移10位减1，到了高22位
		// 状态数
		int status = 1 << cap; // 第一个区间段，可以从0开始的
		// 递增数
		int indexCount = 5;
		log.debug("-------------------------------------------");
		int saveValue = ctlOf(indexCount, status);
		log.debug("需要持久化的数：{} | {}={}", indexCount, status, saveValue);
		log.debug("获取状态数：{}，获取对应的递增数：{}", runStateOf(saveValue), workerCountOf(saveValue));
		log.debug("-------------------------------------------");
		ctl.set(ctlOf(SHUTDOWN, 256));
		log.debug("ctl={}", ctl.get());
		System.out.println("runStateOf=" + runStateOf(ctl.get()));
		System.out.println("workerCountOf=" + workerCountOf(ctl.get()));
		int code = 1_000_000;

	}

	@Test
	public void hex() {
		// 0~15
		for (int i = 0; i <= 0xf; i++) {
			log.info("i={}", i);
		}
		// 0~127
		for (int i = 0; i <= 0x7f; i++) {
			log.info("i={}", i);
		}
	}

	@Test
	public void bit32_4byte() {
		byte[] digest = ByteUtils.toBytes(1024);
		// hash code, 把long截取到32-bits
		long hashCode = ((long) (digest[3] & 0xFF) << 24) | ((long) (digest[2] & 0xFF) << 16)
				| ((long) (digest[1] & 0xFF) << 8) | (digest[0] & 0xFF);
		log.info("hashCode={}", hashCode);
	}

	/**
	 * 时间轮.
	 * @param ticksPerWheel
	 * @return
	 */
	public int normalizeTicksPerWheel(int ticksPerWheel) {
		int normalizedTicksPerWheel = ticksPerWheel - 1;
		normalizedTicksPerWheel |= normalizedTicksPerWheel >>> 1;
		normalizedTicksPerWheel |= normalizedTicksPerWheel >>> 2;
		normalizedTicksPerWheel |= normalizedTicksPerWheel >>> 4;
		normalizedTicksPerWheel |= normalizedTicksPerWheel >>> 8;
		normalizedTicksPerWheel |= normalizedTicksPerWheel >>> 16;
		return normalizedTicksPerWheel + 1;
	}

	@Test
	public void bit32_4byte1() {
		for (int i = 0; i < 8; i++)
			log.info("{}={}", i, normalizeTicksPerWheel(i));

	}

	/**
	 * hash的扩容.
	 */
	@Test
	public void hashExtension() {
		int n = 1024;
		// 相当于0.75*n,使用右移，可以更好的进行整型的计划，速度远大于除法
		int newN = n - (n >>> 2);
		log.info("bin 0.75*n={}", newN);
		log.info("dec 0.75*n={}", n * 0.75);

	}

	@Test
	public void convert() {
		log.info("{}", -128 & 0xff); // 128
		log.info("{}", -2 & 0xff); // 254
		log.info("{}", -1 & 0xff); // 255
		log.info("{}", 0 & 0xff); // 0
		log.info("{}", 1 & 0xff); // 1
		log.info("{}", 127 & 0xff); // 127
		log.info("{}", 128 & 0xff); // 128，与-128是一样的

		System.out.println("* byte字节码与运算原值(-54)换行后(-54 & 0x0FF)：" + (-54 & 0x0FF));

	}

	@Test
	public void pi() {
		// π/4 = 1 - 1/3 + 1/5 - 1/7 + 1/9 - 1/11 + ...
		double sum = 0;
		int flag = 1;
		for (int i = 1; i <= 10000; i++) {
			if (i % 2 == 0) {
				flag = -1;
			}
			else {
				flag = 1;
			}
			sum += (flag) * 1.0 / (i * 2 - 1);
		}
		System.out.println("pi=" + sum * 4);
	}

}
