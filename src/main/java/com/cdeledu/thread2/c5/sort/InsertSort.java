package com.cdeledu.thread2.c5.sort;

//二，插入排序算法分析
//插入排序算法有种递归的思想在里面，它由N-1趟排序组成。初始时，只考虑数组下标0处的元素，只有一个元素，显然是有序的。
//然后第一趟 对下标 1 处的元素进行排序，保证数组[0,1]上的元素有序；
//第二趟 对下标 2 处的元素进行排序，保证数组[0,2]上的元素有序；
public class InsertSort {

	public static <T extends Comparable<? super T>> void insertSort(T[] a) {
		for (int p = 1; p < a.length; p++) {
			T tmp = a[p];// 保存当前位置p的元素，其中[0,p-1]已经有序
			int j;
			for (j = p; j > 0 && tmp.compareTo(a[j - 1]) < 0; j--) {
				a[j] = a[j - 1];// 后移一位
			}
			a[j] = tmp;// 插入到合适的位置
			for (T i : a) {
				System.out.print(i + " ");
			}
			System.out.println();
		}
	}

	// for test purpose
	public static void main(String[] args) {
		Integer[] arr = { 9,8,7,6 };
		insertSort(arr);
		for (Integer i : arr) {
			System.out.print(i + " ");
		}
	}

}
