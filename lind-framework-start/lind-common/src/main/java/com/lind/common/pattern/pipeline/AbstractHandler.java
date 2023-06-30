package com.lind.common.pattern.pipeline;

/**
 * @author lind
 * @date 2023/6/27 16:14
 * @since 1.0.0
 */
public abstract class AbstractHandler<T> implements Comparable<T> {

	private AbstractHandler nextHandler;

	private double sortNumber;

	public double getSortNumber() {
		return sortNumber;
	}

	public void setSortNumber(double sortNumber) {
		this.sortNumber = sortNumber;
	}

	public final void setNextHandler(AbstractHandler nextHandler) {
		this.nextHandler = nextHandler;
	}

	protected abstract void execute(T body);

	// final表示不允许子类重写
	public final void handleRequest(T body) {
		execute(body);

		if (this.nextHandler != null)
			this.nextHandler.handleRequest(body);
	}

	@Override
	public int compareTo(T o) {
		// 升序
		return Double.compare(this.sortNumber, ((AbstractHandler) o).sortNumber);
	}

}
