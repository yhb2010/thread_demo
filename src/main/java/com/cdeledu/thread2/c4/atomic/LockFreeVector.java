package com.cdeledu.thread2.c4.atomic;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

//无锁的Vector
//使用一个二维数组实现，因为AtomicReferenceArray内部使用Object[]来进行实际数据的存储，这使得动态空间增加特别麻烦，因此使用二维数组的好处就是为了将来可以方便的增加新的元素。
public class LockFreeVector<E> {

	private static final boolean debug = false;
	private static final int FIRST_BUCKET_SIZE = 8;
	private static final int N_BUCKET = 30;
	private final AtomicReferenceArray<AtomicReferenceArray<E>> buckets;

	//定义Descriptor对象，作用是使用CAS操作写入新数据
	static class Descriptor<E> {
		public int size;//vector的长度
		volatile WriteDescriptor<E> writeop;

		public Descriptor(int size, WriteDescriptor<E> writeop) {
			this.size = size;
			this.writeop = writeop;
		}

		public void completeWrite() {
			WriteDescriptor<E> tmpOp = writeop;
			if (tmpOp != null) {
				tmpOp.doIt();
				writeop = null; // this is safe since all write to writeop use null as r_value.
			}
		}
	}

	static class WriteDescriptor<E> {
		public E oldV;//期望值
		public E newV;//需要写入的值
		public AtomicReferenceArray<E> addr;//要修改的原子数组
		public int addr_ind;//要写入数组的索引位置

		public WriteDescriptor(AtomicReferenceArray<E> addr, int addr_ind, E oldV, E newV) {
			this.addr = addr;
			this.addr_ind = addr_ind;
			this.oldV = oldV;
			this.newV = newV;
		}

		public void doIt() {
			addr.compareAndSet(addr_ind, oldV, newV);
		}
	}

	private AtomicReference<Descriptor<E>> descriptor;
	private static final int zeroNumFirst = Integer.numberOfLeadingZeros(FIRST_BUCKET_SIZE);;

	public LockFreeVector() {
		//初始化二维数组：buckets里面放30个数组，第一个数组大小为8，后面数组依次大小翻倍(第二个16，第三个32)
		buckets = new AtomicReferenceArray<AtomicReferenceArray<E>>(N_BUCKET);
		buckets.set(0, new AtomicReferenceArray<E>(FIRST_BUCKET_SIZE));
		descriptor = new AtomicReference<Descriptor<E>>(new Descriptor<E>(0, null));
	}

	public void push_back(E e) {
		Descriptor<E> desc;
		Descriptor<E> newd;
		do {
			desc = descriptor.get();
			//使用descriptor先将数据写入数组，是为了防止上一个线程设置完descriptor后(while (!descriptor.compareAndSet(desc, newd)))，还没来得及执行写入(descriptor.get().completeWrite())，因此，做一次预防性的操作
			desc.completeWrite();
			//desc.size   Vector 本身的大小
			//FIRST_BUCKET_SIZE  第一个一维数组的大小
			int pos = desc.size + FIRST_BUCKET_SIZE;
			//该函数的功能  在指定 int 值的二进制补码表示形式中最高位（最左边）的 1 位之前，返回零位的数量。如果指定值在其二进制补码表示形式中不存在 1 位，换句话说，如果它等于零，则返回 32。
			int zeroNumPos = Integer.numberOfLeadingZeros(pos);  // 取出pos 的前导零
			//zeroNumFirst  为FIRST_BUCKET_SIZE 的前导零
			int bucketInd = zeroNumFirst - zeroNumPos;  //哪个一维数组
			//判断这个一维数组是否已经启用
			if (buckets.get(bucketInd) == null) {
				//newLen  一维数组的长度
				int newLen = 2 * buckets.get(bucketInd - 1).length();
				if (debug)
					System.out.println("New Length is:" + newLen);
				buckets.compareAndSet(bucketInd, null, new AtomicReferenceArray<E>(newLen));
			}
			int idx = (0x80000000>>>zeroNumPos) ^ pos;   //在这个一维数组中，我在哪个位置
			newd = new Descriptor<E>(desc.size + 1, new WriteDescriptor<E>(buckets.get(bucketInd), idx, null, e));
		} while (!descriptor.compareAndSet(desc, newd));
		//将数据真正写入数组
		descriptor.get().completeWrite();
	}

	public E get(int index) {
		int pos = index + FIRST_BUCKET_SIZE;
		int zeroNumPos = Integer.numberOfLeadingZeros(pos);
		int bucketInd = zeroNumFirst - zeroNumPos;
		int idx = (0x80000000>>>zeroNumPos) ^ pos;
		return buckets.get(bucketInd).get(idx);
	}

	public static void main(String[] args) {
		LockFreeVector<Integer> v = new LockFreeVector<>();
		v.push_back(1);
		v.push_back(2);
		v.push_back(3);
		v.push_back(4);
		v.push_back(5);
		v.push_back(6);
		v.push_back(7);
		v.push_back(8);
		v.push_back(9);
		v.push_back(10);
		v.push_back(11);
		v.push_back(12);
		v.push_back(13);
		v.push_back(14);
		v.push_back(15);
		v.push_back(16);
		v.push_back(17);
		v.push_back(18);
		v.push_back(19);
		v.push_back(20);
		v.push_back(21);
		v.push_back(22);
		v.push_back(23);
		v.push_back(24);
		v.push_back(25);
	}

}
