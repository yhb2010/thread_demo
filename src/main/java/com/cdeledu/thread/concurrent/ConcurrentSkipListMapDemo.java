package com.cdeledu.thread.concurrent;

import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

//java.util.concurrent.ConcurrentNavigableMap 是一个支持并发访问的 java.util.NavigableMap，它还能让它的子 map 具备并发访问的能力。所谓的 "子 map" 指的是诸如 headMap()，subMap()，tailMap() 之类的方法返回的 map。
//headMap(K toKey)：K是参数化ConcurrentSkipListMap对象的Key值的类。返回此映射的部分视图，其键值小于 toKey。
//tailMap(K fromKey)：K是参数化ConcurrentSkipListMap对象的Key值的类。返回此映射的部分视图，其键大于等于 fromKey。

//跳表查找：
//如果我们想查找19是否存在？如何查找呢？我们从头结点开始，首先和9进行判断，此时大于9，然后和21进行判断，小于21，此时这个值肯定在9结点和21结点之间，此时，我们和17进行判断，大于17，然后和21进行判断，小于21，此时肯定在17结点和21结点之间，此时和19进行判断，找到了。具体的示意图如图所示：
//concurrentHashMap与ConcurrentSkipListMap性能测试
//在4线程1.6万数据的条件下，ConcurrentHashMap 存取速度是ConcurrentSkipListMap 的4倍左右。
//但ConcurrentSkipListMap有几个ConcurrentHashMap 不能比拟的优点：
//1、ConcurrentSkipListMap 的key是有序的。
//2、ConcurrentSkipListMap 支持更高的并发。ConcurrentSkipListMap 的存取时间是log（N），和线程数几乎无关。也就是说在数据量一定的情况下，并发的线程越多，ConcurrentSkipListMap越能体现出他的优势。
//二、使用建议
//在非多线程的情况下，应当尽量使用TreeMap。此外对于并发性相对较低的并行程序可以使用Collections.synchronizedSortedMap将TreeMap进行包装，也可以提供较好的效率。对于高并发程序，应当使用ConcurrentSkipListMap，能够提供更高的并发度。
//所以在多线程程序中，如果需要对Map的键值进行排序时，请尽量使用ConcurrentSkipListMap，可能得到更好的并发度。
//注意，调用ConcurrentSkipListMap的size时，由于多个线程可以同时对映射表进行操作，所以映射表需要遍历整个链表才能返回元素个数，这个操作是个O(log(n))的操作。
public class ConcurrentSkipListMapDemo {

	public static void main(String[] args) {
		ConcurrentSkipListMap<String, Contact> map;
		map = new ConcurrentSkipListMap<>();
		Thread threads[] = new Thread[25];
		int counter = 0;

		for (char i = 'A'; i < 'Z'; i++) {
			Task task = new Task(map, String.valueOf(i));
			threads[counter] = new Thread(task);
			threads[counter].start();
			counter++;
		}

		// 使用join()方法等待线程的结束。
		for (int i = 0; i < 25; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// 使用firstEntry()方法获取map的第一个实体，并将它的数据写入到控制台。
		System.out.printf("Main: Size of the map: %d\n", map.size());
		Map.Entry<String, Contact> element;
		Contact contact;
		element = map.firstEntry();
		contact = element.getValue();
		System.out.printf("Main: First Entry: %s: %s\n", contact.getName(), contact.getPhone());
		// 使用lastEntry()方法获取map的最后一个实体，并将它的数据写入到控制台。
		element = map.lastEntry();
		contact = element.getValue();
		System.out.printf("Main: Last Entry: %s: %s\n", contact.getName(), contact.getPhone());
		// 使用subMap()方法获取map的子map，并将它们的数据写入到控制台。
		System.out.printf("Main: Submap from A1996 to B1002: \n");
		ConcurrentNavigableMap<String, Contact> submap = map.subMap("A1996", "B1002");
		do {
			element = submap.pollFirstEntry();
			if (element != null) {
				contact = element.getValue();
				System.out.printf("%s: %s\n", contact.getName(), contact.getPhone());
			}
		} while (element != null);

	}

}
