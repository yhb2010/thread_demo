package com.cdeledu.thread2.c5.disruptor;

/**
 * 上述代码的逻辑很简单，就是四个线程修改一数组不同元素的内容。元素的类型是 VolatileLong，只有一个长整型成员 value 和 6 个没用到的长整型成员。value 设为 volatile 是为了让 value 的修改对所有线程都可见。程序分两种情况执行，第一种情况为不屏蔽倒数第三行（见"屏蔽此行"字样），第二种情况为屏蔽倒数第三行。为了"保证"数据的相对可靠性，程序取 10 次执行的平均时间。
 * 两个逻辑一模一样的程序，前者的耗时大概是后者的 2.5 倍，这太不可思议了！那么这个时候，我们再用伪共享（False Sharing）的理论来分析一下。前者 longs 数组的 4 个元素，由于 VolatileLong 只有 1 个长整型成员，所以整个数组都将被加载至同一缓存行，但有4个线程同时操作这条缓存行，于是伪共享就悄悄地发生了。
 * 基于此，我们有理由相信，在一定线程数量范围内（注意思考：为什么强调是一定线程数量范围内），随着线程数量的增加，伪共享发生的频率也越大，直观体现就是执行时间越长。为了证实这个观点，本人在同样的机器上分别用单线程、2、4、8个线程，对有填充和无填充两种情况进行测试。执行场景是取 10 次执行的平均时间，结果如下所示：
 * @author DELL
 *
 */
public class FalseShareTest implements Runnable {

	public static int NUM_THREADS = 4;
    public final static long ITERATIONS = 500L * 1000L * 1000L;
    private final int arrayIndex;
    private static VolatileLong[] longs;
    public static long SUM_TIME = 0l;

    public FalseShareTest(final int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }
    public static void main(final String[] args) throws Exception {
        Thread.sleep(10000);
        for(int j=0; j<10; j++){
            System.out.println(j);
            if (args.length == 1) {
                NUM_THREADS = Integer.parseInt(args[0]);
            }
            longs = new VolatileLong[NUM_THREADS];
            for (int i = 0; i < longs.length; i++) {
                longs[i] = new VolatileLong();
            }
            final long start = System.nanoTime();
            runTest();
            final long end = System.nanoTime();
            SUM_TIME += end - start;
        }
        System.out.println("平均耗时："+SUM_TIME/10);
    }
    private static void runTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseShareTest(i));
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }
    public void run() {
        long i = ITERATIONS + 1;
        while (0 != --i) {
            longs[arrayIndex].value = i;
        }
    }
    public final static class VolatileLong {
        public volatile long value = 0L;
        //public long p1, p2, p3, p4, p5, p6;     //屏蔽此行
    }

}
