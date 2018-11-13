package com.cdeledu.thread3.c29event_driven.async;

import java.util.concurrent.TimeUnit;

import com.cdeledu.thread3.c29event_driven.sync.Event;

public class AsyncEventDispatchExample {
	
	static class InputEvent extends Event {
		private final int x;
		private final int y;
		public InputEvent(int x, int y) {
			this.x = x;
			this.y = y;
		}
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
	}
	
	static class ResultEvent extends Event {
		private final int result;
		public ResultEvent(int result) {
			this.result = result;
		}
		public int getResult() {
			return result;
		}
	}
	
	static class AsyncResultEventHandler extends AsyncChannel {
		@Override
		protected void handle(Event message) {
			ResultEvent resultEvent = (ResultEvent)message;
			try{
				TimeUnit.SECONDS.sleep(4);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			System.out.println("the result is:" + resultEvent.getResult());
		}
	}
	
	//主要用于处理InputEvent，但是需要继承AsyncChannel
	static class AsyncInputEventHandler extends AsyncChannel {
		private final AsyncEventDispatcher dispatcher;
		public AsyncInputEventHandler(AsyncEventDispatcher dispatcher) {
			this.dispatcher = dispatcher;
		}
		//不同于以同步的方式实现dispatch，异步的方式需要实现handle
		@Override
		protected void handle(Event message) {
			InputEvent inputEvent = (InputEvent)message;
			System.out.printf("X:%d, Y:%d\n", inputEvent.getX(), inputEvent.getY());
			try{
				TimeUnit.SECONDS.sleep(2);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			int result = inputEvent.getX() + inputEvent.getY();
			dispatcher.dispatch(new ResultEvent(result));
		}
	}
	
	/**InputEvent是一个Message，它包含了两个int类型的属性，而InputEventHandler是对InputEvent消息的处理，接收到了InputEvent消息之后，分别对
	 * X和Y进行相加操作，然后将结果封装成ResultEvent提交给EventDispatcher，ResultEvent相对比较简单，只包含了计算结果的属性，ResultEventHandler
	 * 则将计算结果输出到控制台。
	 * 不同的数据处理之间不需要知道彼此的存在，一切都由EventDispatcher这个Router来控制，他会给你想要的一切，这是一种稀疏耦合的设计。
	 * 扩展性也是很好，比如Channel容易扩展和替换，另外由于Dispatcher统一负责Event的调配，因此在消息通过Channel之前可以进行很多过滤、数据验证、权限控制、数据增强等工作。
	 * @param args
	 */
	public static void main(String[] args) {
		//构造Router
		AsyncEventDispatcher dispathcer = new AsyncEventDispatcher();
		//将Event和Handler(Channel)的绑定关系注册到Dispatcher
		dispathcer.registerChannel(InputEvent.class, new AsyncInputEventHandler(dispathcer));
		dispathcer.registerChannel(ResultEvent.class, new AsyncResultEventHandler());
		dispathcer.dispatch(new InputEvent(1, 2));
		dispathcer.dispatch(new InputEvent(10, 20));
		System.out.println("---------------");
	}

}
