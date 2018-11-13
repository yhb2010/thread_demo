package com.cdeledu.高并发的大杀器异步化并行化.parallel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.cdeledu.高并发的大杀器异步化并行化.CustomerInfo;
import com.cdeledu.高并发的大杀器异步化并行化.DiscountInfo;
import com.cdeledu.高并发的大杀器异步化并行化.FoodListInfo;
import com.cdeledu.高并发的大杀器异步化并行化.OrderInfo;
import com.cdeledu.高并发的大杀器异步化并行化.OtherInfo;
import com.cdeledu.高并发的大杀器异步化并行化.TenantInfo;

/**CountDownLatch 虽然能实现我们需要满足的功能但是其仍然有个问题是，我们的业务代码需要耦合 CountDownLatch 的代码。
比如在我们获取用户信息之后，我们会执行 countDownLatch.countDown()，很明显我们的业务代码显然不应该关心这一部分逻辑，并且在开发的过程中万一写漏了，那我们的 await 方法将只会被各种异常唤醒。
请看CompletableFuture
 * @author DELL
 *
 */
public class CountDownTask {

	private static final int CORE_POOL_SIZE = 4;
	   private static final int MAX_POOL_SIZE = 12;
	   private static final long KEEP_ALIVE_TIME = 5L;
	   private final static int QUEUE_SIZE = 1600;

	   protected final static ExecutorService THREAD_POOL = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
	           KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<>(QUEUE_SIZE));
	   public static void main(String[] args) throws InterruptedException {
	       // 新建一个为5的计数器
	       CountDownLatch countDownLatch = new CountDownLatch(5);
	       OrderInfo orderInfo = new OrderInfo();
	       THREAD_POOL.execute(() -> {
	           System.out.println("当前任务Customer,线程名字为:" + Thread.currentThread().getName());
	           orderInfo.setCustomerInfo(new CustomerInfo());
	           countDownLatch.countDown();
	       });
	       THREAD_POOL.execute(() -> {
	           System.out.println("当前任务Discount,线程名字为:" + Thread.currentThread().getName());
	           orderInfo.setDiscountInfo(new DiscountInfo());
	           countDownLatch.countDown();
	       });
	       THREAD_POOL.execute(() -> {
	           System.out.println("当前任务Food,线程名字为:" + Thread.currentThread().getName());
	           orderInfo.setFoodListInfo(new FoodListInfo());
	           countDownLatch.countDown();
	       });
	       THREAD_POOL.execute(() -> {
	           System.out.println("当前任务Tenant,线程名字为:" + Thread.currentThread().getName());
	           orderInfo.setTenantInfo(new TenantInfo());
	           countDownLatch.countDown();
	       });
	       THREAD_POOL.execute(() -> {
	           System.out.println("当前任务OtherInfo,线程名字为:" + Thread.currentThread().getName());
	           orderInfo.setOtherInfo(new OtherInfo());
	           countDownLatch.countDown();
	       });
	       countDownLatch.await(1, TimeUnit.SECONDS);
	       System.out.println("主线程："+ Thread.currentThread().getName());
	   }

}
