package com.cdeledu.thread.single;

/**
 * 第一个注意点：使用私有的构造函数，确保正常情况下该类不能被外部初始化（非正常情况比如通过反射初始化，一般使用反射之后单例模式也就失去效果了）。
第二个注意点：getInstance方法中第一个判空条件，逻辑上是可以去除的，去除之后并不影响单例的正确性，但是去除之后效率低。因为去掉之后，不管instance是
否已经初始化，都会进行synchronized操作，而synchronized是一个重操作消耗性能。加上之后，如果已经初始化直接返回结果，不会进行synchronized操作。
第三个注意点：加上synchronized是为了防止多个线程同时调用getInstance方法时，各初始化instance一遍的并发问题。
第四个注意点：getInstance方法中的第二个判空条件是不可以去除，如果去除了，并且刚好有两个线程a和b都通过了第一个判空条件。此时假设a先获得锁，进入
synchronized的代码块，初始化instance，a释放锁。接着b获得锁，进入synchronized的代码块，也直接初始化instance，instance被初始化多遍不符合单例
模式的要求~。加上第二个判空条件之后，b获得锁进入synchronized的代码块，此时instance不为空，不执行初始化操作。
第五个注意点：instance的声明有一个volatile关键字，如果不用该关键字，有可能会出现异常。因为instance = new Test();并不是一个原子操作，会被编译成
三条指令：1.给Test的实例分配内存 2.初始化Test的构造器 3.将instance对象指向分配的内存空间（注意 此时instance就不为空）
然后，java会指令重排序，JVM根据处理器的特性，充分利用多级缓存，多核等进行适当的指令重排序，使程序在保证业务运行的同时，充分利用CPU的执行特点，最大
发挥机器的性能！简单来说就是jvm执行上面三条指令的时候，不一定是1-2-3这样执行，有可能是1-3-2这样执行。如果jvm是按照1-3-2来执行的话，当1-3执行完2
还没执行的时候，如果另外一个线程调用getInstance()，因为3执行了此时instance不为空，直接返回instance。问题是2还没执行，此时instance相当于什么都
没有，肯定是有问题的。然后咧，volatile有一个特性就是禁止指令重排序，上面的三条指令是按照1-2-3执行的，这样就没有问题了。
 * @author DELL
 *
 */
public class Test {
    private volatile static Test instance;

    private Test() {

    }

    public static Test getInstance() {
        if (instance == null) {
            synchronized (Test.class) {
                if (instance == null) {
                    instance = new Test();
                }
            }
        }
        return instance;
    }
}