package com.lind.common.pattern.squirrel;

public class StateMachineExample {

	enum State {

		OFF_HOOK, RINGING, CONNECTED, ON_HOLD

	}

	enum Trigger {

		CALL_DIALED, HUNG_UP, CALL_CONNECTED, PLACED_ON_HOLD, TAKEN_OFF_HOLD, LEFT_MESSAGE, STOP_USING_PHONE

	}

}
