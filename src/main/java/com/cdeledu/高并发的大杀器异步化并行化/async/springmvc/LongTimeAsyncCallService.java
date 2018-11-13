package com.cdeledu.高并发的大杀器异步化并行化.async.springmvc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class LongTimeAsyncCallService {

	private final int CorePoolSize = 4;
    private final long NeedSeconds = 3;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(CorePoolSize);

    public void makeRemoteCallAndUnknownWhenFinish(LongTermTaskCallback callback){
        System.out.println(System.currentTimeMillis() + "完成此任务需要 : " + NeedSeconds + " 秒");
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                callback.callback("长时间异步调用完成.");
            }
        }, NeedSeconds, TimeUnit.SECONDS);
    }

}
