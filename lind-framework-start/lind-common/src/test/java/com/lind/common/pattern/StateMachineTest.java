package com.lind.common.pattern;

import org.junit.Test;

/**
 * 状态机.
 *
 * @author lind
 * @date 2023/2/28 14:00
 * @since 1.0.0
 */
public class StateMachineTest {

	/**
	 * 根据状态State的不同，行走的逻辑也是不同的.
	 */
	@Test
	public void test() {
		AppContext context = new AppContext();
		context.favourite();
		context.updatePwd();
	}

	public static abstract class State {

		private AppContext context;

		public AppContext getContext() {
			return context;
		}

		public void setContext(AppContext context) {
			this.context = context;
		}

		abstract void favourite();

		abstract void updatePwd();

	}

	public static class GuestState extends State {

		@Override
		void favourite() {
			forwardToLoginPage();
			this.getContext().favourite();
		}

		@Override
		void updatePwd() {
			forwardToLoginPage();
			this.getContext().updatePwd();
		}

		private void forwardToLoginPage() {
			System.out.println("跳转到登陆页面");
			this.getContext().setState(AppContext.STATE_LOGIN);
		}

	}

	public static class LoginState extends State {

		@Override
		void favourite() {
			System.out.println("登陆成功，跳转到首页");
			System.out.println("收藏成功!");
		}

		@Override
		void updatePwd() {
			System.out.println("修改安全信息，需要再次验证您的身份！");
			this.getContext().setState(AppContext.STATE_REAUTH);
			this.getContext().updatePwd();
		}

	}

	public static class ReauthState extends State {

		@Override
		void favourite() {
			System.out.println("收藏成功！");
		}

		@Override
		void updatePwd() {
			System.out.println("密码修改成功！");
		}

	}

	public static class AppContext {

		protected static final State STATE_GUEST = new GuestState();

		protected static final State STATE_LOGIN = new LoginState();

		protected static final State STATE_REAUTH = new ReauthState();

		private State currentSate = STATE_GUEST;

		{
			STATE_GUEST.setContext(this);
			STATE_LOGIN.setContext(this);
			STATE_REAUTH.setContext(this);
		}

		public void setState(State state) {
			this.currentSate = state;
		}

		public void favourite() {
			this.currentSate.favourite();
		}

		public void updatePwd() {
			this.currentSate.updatePwd();
		}

	}

}
