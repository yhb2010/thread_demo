package com.cdeledu.高并发的大杀器异步化并行化.parallel;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import com.cdeledu.高并发的大杀器异步化并行化.CustomerInfo;
import com.cdeledu.高并发的大杀器异步化并行化.DiscountInfo;
import com.cdeledu.高并发的大杀器异步化并行化.FoodListInfo;
import com.cdeledu.高并发的大杀器异步化并行化.OrderInfo;
import com.cdeledu.高并发的大杀器异步化并行化.OtherInfo;
import com.cdeledu.高并发的大杀器异步化并行化.TenantInfo;

/**我们定义一个 Order Task 并且定义五个获取信息的任务，在 Compute 中分别 Fork 执行这五个任务，最后在将这五个任务的结果通过 Join 获得，最后完成我们的并行化的需求。
 * @author DELL
 *
 */
public class OrderTask extends RecursiveTask<OrderInfo> {
	   @Override
	   protected OrderInfo compute() {
	       System.out.println("执行"+ this.getClass().getSimpleName() + "线程名字为:" + Thread.currentThread().getName());
	       // 定义其他五种并行TasK
	       CustomerTask customerTask = new CustomerTask();
	       TenantTask tenantTask = new TenantTask();
	       DiscountTask discountTask = new DiscountTask();
	       FoodTask foodTask = new FoodTask();
	       OtherTask otherTask = new OtherTask();
	       invokeAll(customerTask, tenantTask, discountTask, foodTask, otherTask);
	       OrderInfo orderInfo = new OrderInfo(customerTask.join(), tenantTask.join(), discountTask.join(), foodTask.join(), otherTask.join());
	       return orderInfo;
	   }
	   public static void main(String[] args) {
	       ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() -1 );
	       System.out.println(forkJoinPool.invoke(new OrderTask()));
	   }
	}
	class CustomerTask extends RecursiveTask<CustomerInfo>{

	   @Override
	   protected CustomerInfo compute() {
	       System.out.println("执行"+ this.getClass().getSimpleName() + "线程名字为:" + Thread.currentThread().getName());
	       return new CustomerInfo();
	   }
	}
	class TenantTask extends RecursiveTask<TenantInfo>{

	   @Override
	   protected TenantInfo compute() {
	       System.out.println("执行"+ this.getClass().getSimpleName() + "线程名字为:" + Thread.currentThread().getName());
	       return new TenantInfo();
	   }
	}
	class DiscountTask extends RecursiveTask<DiscountInfo>{

	   @Override
	   protected DiscountInfo compute() {
	       System.out.println("执行"+ this.getClass().getSimpleName() + "线程名字为:" + Thread.currentThread().getName());
	       return new DiscountInfo();
	   }
	}
	class FoodTask extends RecursiveTask<FoodListInfo>{

	   @Override
	   protected FoodListInfo compute() {
	       System.out.println("执行"+ this.getClass().getSimpleName() + "线程名字为:" + Thread.currentThread().getName());
	       return new FoodListInfo();
	   }
	}
	class OtherTask extends RecursiveTask<OtherInfo>{

	   @Override
	   protected OtherInfo compute() {
	       System.out.println("执行"+ this.getClass().getSimpleName() + "线程名字为:" + Thread.currentThread().getName());
	       return new OtherInfo();
	   }
	}