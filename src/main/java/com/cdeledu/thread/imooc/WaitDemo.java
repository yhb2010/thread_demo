package com.cdeledu.thread.imooc;

public class WaitDemo {

	public static boolean isFinish = false;
    public static Object object = new Object();

    public static void main(String[] args) {

        // 下载图片的线程
        final Thread download = new Thread() {
            public void run() {
                System.out.println("download:开始下载图片");
                for(int i = 0; i <= 100; i++){
                    System.out.println("download:已完成" + i + "%");
                    try{
                        Thread.sleep(50);
                    }
                    catch(InterruptedException e){
                    }
                }
                System.out.println("download:图片下载完毕");
                isFinish = true;// 表示图片下载完毕了

                // 当图片下载完毕后，就可以通知showImg开始显示图片了
                synchronized(object){
                    // 通知在object身上等待的线程解除等待阻塞
                    object.notify();
                }

                System.out.println("download:开始下载附件");
                for(int i = 0; i <= 100; i++){
                    System.out.println("download:已完成" + i + "%");
                    try{
                        Thread.sleep(50);
                    }
                    catch(InterruptedException e){
                    }
                }
                System.out.println("download:附件下载完毕");
            }

        };

        // 用于显示图片的线程
        Thread showImg = new Thread() {
            public void run() {
                System.out.println("show:准备显示图片");
                // 等待下载线程将图片下载结束后，再执行下面的代码
                try{
                    // wait()阻塞会在以下两种情况被解除,1:当download线程结束. 2:当调用了download的notify()
                    synchronized(object){
                        object.wait();
                    }
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                if(!isFinish){
                    throw new RuntimeException("图片没有下载完毕");
                }
                try{
                    Thread.sleep(300);
                }
                catch(InterruptedException e){
                }
                System.out.println("show:图片已经显示了!");
            }
        };

        download.start();
        showImg.start();
    }

}
