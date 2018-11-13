package com.cdeledu.高并发的大杀器异步化并行化.async.springmvc;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;

@RestController
public class LongTimeAsyncCallController {

	@Autowired
	private LongTimeAsyncCallService longTimeAsyncCallService;
	@Autowired
	@Qualifier("taskExecutor")
	private ThreadPoolTaskExecutor executor;

	@GetMapping("/asynctask")
	public DeferredResult<String> asyncTask(){
	    DeferredResult<String> deferredResult = new DeferredResult<String>(2000l);
	    System.out.println(System.currentTimeMillis() + "/asynctask 调用！thread id is : " + Thread.currentThread().getId());
	    longTimeAsyncCallService.makeRemoteCallAndUnknownWhenFinish(new LongTermTaskCallback() {
	        @Override
	        public void callback(Object result) {
	            System.out.println(System.currentTimeMillis() + "异步调用执行完成, thread id is : " + Thread.currentThread().getId());
	            deferredResult.setResult(result.toString());
	        }
	    });
	    System.out.println(System.currentTimeMillis() + "========中间过程==========");
		deferredResult.onTimeout(new Runnable() {
			@Override
			public void run() {
				System.out.println(System.currentTimeMillis() + "异步调用执行超时！thread id is : " + Thread.currentThread().getId());
				deferredResult.setResult("超时了");
			}
		});
	    return deferredResult;
	}

	@GetMapping("/asynctask2")
	public WebAsyncTask<String> asyncTaskCompletion() {
	    // 打印处理线程名
	    System.out.println(System.out.format(System.currentTimeMillis() + "请求处理线程：%s", Thread.currentThread().getName()));

	    // 模拟开启一个异步任务，超时时间为10s
	    WebAsyncTask<String> asyncTask = new WebAsyncTask<>(3 * 1000L, () -> {
	    	System.out.println(System.out.format(System.currentTimeMillis() + "异步工作线程：%s", Thread.currentThread().getName()));
	        // 任务处理时间5s，不超时
	        TimeUnit.SECONDS.sleep(2);
	        return "ok";
	    });

	    // 任务执行完成时调用该方法
	    asyncTask.onCompletion(() -> System.out.println(System.currentTimeMillis() + "任务执行完成"));
	    System.out.println(System.currentTimeMillis() + "继续处理其他事情");
	    return asyncTask;
	}

	@GetMapping("/asynctask3")
	public WebAsyncTask<String> asyncTaskException() {
	    // 打印处理线程名
		System.out.println(System.out.format(System.currentTimeMillis() + "请求处理线程：%s", Thread.currentThread().getName()));

	    // 模拟开启一个异步任务，超时时间为10s
	    WebAsyncTask<String> asyncTask = new WebAsyncTask<>(3 * 1000L, () -> {
	    	System.out.println(System.out.format(System.currentTimeMillis() + "异步工作线程：%s", Thread.currentThread().getName()));
	        // 任务处理时间5s，不超时
	    	TimeUnit.SECONDS.sleep(2);
	        throw new Exception("error");
	    });

	    // 任务执行完成时调用该方法
	    asyncTask.onCompletion(() -> System.out.println(System.currentTimeMillis() + "任务执行完成"));
	    asyncTask.onError(() -> {
	    	System.out.println(System.currentTimeMillis() + "任务执行异常");
	        return "error";
	    });

	    System.out.println(System.currentTimeMillis() + "继续处理其他事情");
	    return asyncTask;
	}

	@GetMapping("/timeout")
	public WebAsyncTask<String> asyncTaskTimeout() {
	    // 打印处理线程名
		System.out.println(System.out.format(System.currentTimeMillis() + "请求处理线程：%s", Thread.currentThread().getName()));

	    // 模拟开启一个异步任务，超时时间为10s
	    WebAsyncTask<String> asyncTask = new WebAsyncTask<>(2 * 1000L, () -> {
	    	System.out.println(System.out.format(System.currentTimeMillis() + "异步工作线程：%s", Thread.currentThread().getName()));
	        // 任务处理时间5s，不超时
	    	TimeUnit.SECONDS.sleep(3);
	        return "ok";
	    });

	    // 任务执行完成时调用该方法
	    asyncTask.onCompletion(() -> System.out.println(System.currentTimeMillis() + "任务执行完成"));
	    asyncTask.onTimeout(() -> {
	    	System.out.println(System.currentTimeMillis() + "任务执行超时");
	        return "超时";
	    });

	    System.out.println(System.currentTimeMillis() + "继续处理其他事情");
	    return asyncTask;
	}

	@GetMapping("/threadPool")
	public WebAsyncTask<String> asyncTaskThreadPool() {
		WebAsyncTask<String> asyncTask = new WebAsyncTask<>(10 * 1000L, executor,
	            () -> {
	            	TimeUnit.SECONDS.sleep(2);
	            	System.out.println(System.out.format(System.currentTimeMillis() + "异步工作线程：%s", Thread.currentThread().getName()));
	                return "ok";
	            });
		System.out.println(System.currentTimeMillis() + "继续处理其他事情");
		return asyncTask;
	}

}
