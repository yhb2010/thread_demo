package com.cdeledu.thread2.c5.sort;

import java.util.Arrays;

/**
希尔排序是把记录按下标的一定增量分组，对每组使用直接插入排序算法排序；随着增量逐渐减少，每组包含的关键词越来越多，当增量减至1时，整个文件恰被分成一组，算法便终止。
 */
public class ShellSort {
    public static void main(String []args){
        int []arr = {1, 4, 10, 20, 6, 3, 2, 8, 9, 5, 12, 18};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 希尔排序 针对有序序列在插入时采用移动法。
     * @param arr
     */
    public static void sort(int []arr){
        //增量gap，并逐步缩小增量
        for(int gap = arr.length/2; gap > 0; gap /= 2){
            //从第gap个元素，逐个对其所在组进行直接插入排序操作
            for(int i = gap; i < arr.length; i++){
                int j = i;
                int temp = arr[j];
                if(arr[j] < arr[j-gap]){
                    while(j-gap >= 0 && temp < arr[j-gap]){
                        //移动法
                        arr[j] = arr[j-gap];
                        j -= gap;
                    }
                    arr[j] = temp;
                }
            }
            System.out.println("gap=" + gap + ":" + Arrays.toString(arr));
        }
    }

}