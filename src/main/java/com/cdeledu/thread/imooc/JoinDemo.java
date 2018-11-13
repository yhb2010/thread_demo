package com.cdeledu.thread.imooc;

public class JoinDemo {

	// 判断照片是否下载完成
    public static boolean isFinish = false;

    public static void main(String[] args) {
        // 下载图片的线程
        final Thread download = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("download:开始下载图片");
                for(int i = 0; i <= 100; i++){
                    System.out.println("download:已完成" + i + "%");
                    try{
                        Thread.sleep(50);
                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
                System.out.println("download:图片下载完毕");
                isFinish = true;
            }
        });
        download.start();

        // 用于显示图片的线程
        Thread showImg = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("show:准备显示图片");
                // 等待下载线程工作结束后,再执行下面的代码,
                try{
                    // 此时显示图片的线程就进入阻塞状态,等待download线程运行结束,才能执行下面的代码。注意千万不要在永远也死不了的线程上等待
                    download.join();
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                if(!isFinish){
                    throw new RuntimeException("show:图片还没有下载完");
                }
                System.out.println("show:图片显示完成！");
            }
        });
        showImg.start();
    }

}
