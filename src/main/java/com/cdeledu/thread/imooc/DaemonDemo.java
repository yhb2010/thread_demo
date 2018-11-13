package com.cdeledu.thread.imooc;

public class DaemonDemo {

	public static void main(String[] args) {

        // Rose,表演者:前台线程
        Thread rose = new Thread() {
            public void run() {
                for(int i = 0; i < 10; i++){
                    System.out.println("rose:let me go!");
                    try{
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
                System.out.println("rose:啊啊啊啊AAAAAAaaaaaa....");
                System.out.println("噗通！");
            }
        };

        // jack,表演者:后台线程
        Thread jack = new Thread() {
            public void run() {
                while(true){
                    System.out.println("jack:you jump!i jump!");
                    try{
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        /*
         * 设置为后台线程
         * 设置后台线程的方法要在该线程被调用start()方法之前调用
         */
        jack.setDaemon(true);
        rose.start();
        jack.start();
	}

}
