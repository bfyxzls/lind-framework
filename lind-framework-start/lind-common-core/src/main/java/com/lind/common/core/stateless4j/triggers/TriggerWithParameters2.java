package com.lind.common.core.stateless4j.triggers;

public class TriggerWithParameters2<TArg0, TArg1, TTrigger> extends TriggerWithParameters<TTrigger> {

	/**
	 * Create a configured trigger
	 * @param underlyingTrigger Trigger represented by this trigger configuration
	 * @param classe0 Class argument
	 * @param classe1 Class argument
	 */
	public TriggerWithParameters2(TTrigger underlyingTrigger, Class<TArg0> classe0, Class<TArg1> classe1) {
		super(underlyingTrigger, classe0, classe1);
	}

}
