package com.lind.common.core.stateless4j.triggers;

public class TriggerWithParameters1<TArg0, TTrigger> extends TriggerWithParameters<TTrigger> {

	/**
	 * Create a configured trigger
	 * @param underlyingTrigger Trigger represented by this trigger configuration
	 * @param classe Class argument
	 */
	public TriggerWithParameters1(TTrigger underlyingTrigger, Class<TArg0> classe) {
		super(underlyingTrigger, classe);
	}

}
