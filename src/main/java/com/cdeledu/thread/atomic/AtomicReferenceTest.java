package com.cdeledu.thread.atomic;

import java.util.concurrent.atomic.AtomicReference;

//AtomicReference 提供了一个可以被原子性读和写的对象引用变量。原子性的意思是多个想要改变同一个 AtomicReference 的线程不会导致 AtomicReference 处于不一致的状态。
//AtomicReference 还有一个 compareAndSet() 方法，通过它你可以将当前引用于一个期望值(引用)进行比较，如果相等，在该 AtomicReference 对象内部设置一个新的引用。
public class AtomicReferenceTest {

    public static void main(String[] args){

        // 创建两个Person对象，它们的id分别是101和102。
        Person p1 = new Person(101);
        Person p2 = new Person(102);
        // 新建AtomicReference对象，初始化它的值为p1对象
        AtomicReference<Person> ar = new AtomicReference<>(p1);
        // 通过CAS设置ar。如果ar的值为p1的话，则将其设置为p2。
        ar.compareAndSet(p1, p2);

        Person p3 = (Person)ar.get();
        System.out.println("p3 is "+p3);
        System.out.println("p3.equals(p1)="+p3.equals(p1));
        System.out.println("p3.equals(p2)="+p3.equals(p2));
    }
}

class Person {
    volatile long id;
    public Person(long id) {
        this.id = id;
    }
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (id != other.id)
			return false;
		return true;
	}
	public String toString() {
        return "id:"+id;
    }
}