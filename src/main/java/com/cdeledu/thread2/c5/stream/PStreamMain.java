package com.cdeledu.thread2.c5.stream;

/**(B+C)*B/2这个计算不能并行执行，因为必须是B+C完成后，才能计算*B/2，但如果有很多这种计算要做，我们可以利用流水线的思想做并行：
 * 一个线程只计算A=B+C，另一个只计算D=A*B，另一个只计算D/2。
 * @author DELL
 *
 */
public class PStreamMain {

	public static void main(String[] args) {
		new Thread(new Plus()).start();
		new Thread(new Multiply()).start();
		new Thread(new Div()).start();

		for(int i=0;i<1000;i++){
			for(int j=0;j<1000;j++){
				Msg msg = new Msg();
				msg.i = i;
				msg.j = j;
				msg.orgStr = "(("+i+"+"+j+")*"+i+")/2";
				Plus.q.add(msg);
			}
		}
	}

}
