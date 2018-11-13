package com.cdeledu.thread.chapter10;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**scheduleAtFixedRate
ScheduledFuture<?> scheduleAtFixedRate(Runnable command,                                        long initialDelay,                                        long period,                                       TimeUnit unit)
创建并执行一个在给定初始延迟后首次启用的定期操作，后续操作具有给定的周期；也就是将在initialDelay 后开始执行，然后在 initialDelay+period 后执行，接着在 initialDelay + 2 * period 后执行，依此类推。如果任务的任一执行遇到异常，都会取消后续执行。否则，只能通过执行程序的取消或终止方法来终止该任务。
参数：
command - 要执行的任务。
initialDelay - 首次执行的延迟时间。
period - 连续执行之间的周期。
unit - initialDelay 和 period 参数的时间单位。
返回：
表示挂起任务完成的 Future，并且其 get() 方法在取消后将抛出异常。
抛出：
RejectedExecutionException - 如果无法安排执行该任务。
NullPointerException - 如果 command 为 null。
IllegalArgumentException - 如果 period 小于或等于 0。


scheduleWithFixedDelay
ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,                                           long initialDelay,                                           long delay,                                          TimeUnit unit)
创建并执行一个在给定初始延迟后首次启用的定期操作，随后，在每一次执行终止和下一次执行开始之间都存在给定的延迟。如果任务的任一执行遇到异常，就会取消后续执行。否则，只能通过执行程序的取消或终止方法来终止该任务。
参数：
command - 要执行的任务。
initialDelay - 首次执行的延迟时间。
delay - 一次执行终止和下一次执行开始之间的延迟。
unit - initialDelay 和 delay 参数的时间单位。
返回：
表示挂起任务完成的 Future，并且其 get() 方法在取消后将抛出异常。
抛出：
RejectedExecutionException - 如果无法安排执行该任务。
NullPointerException - 如果 command 为 null。
IllegalArgumentException - 如果 delay 小于或等于 0

 *
 */
public class ScheduledThreadPool {

	public static void main(String[] args) {
        ScheduledThreadPoolExecutor exec=new ScheduledThreadPoolExecutor(1);

        exec.scheduleAtFixedRate(new Runnable(){//每隔一段时间就触发异常
            @Override
            public void run() {
            	System.out.println("1：" + System.nanoTime());
                throw new RuntimeException();
            }}, 1000, 5000, TimeUnit.MILLISECONDS);

        exec.scheduleAtFixedRate(new Runnable(){//每隔一段时间打印系统时间，证明两者是互不影响的
            @Override
            public void run() {
            	System.out.println("2：" + System.nanoTime());
            }}, 1000, 2000, TimeUnit.MILLISECONDS);
    }

}
