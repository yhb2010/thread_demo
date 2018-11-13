package com.cdeledu.thread3.c27active_objects;

import com.cdeledu.thread3.c19future.Future;

public interface OrderService {

	/**根据订单id查询订单明细，有入参也有返回值，但是返回类型必须是Future，
	 * 因为方法的执行是在其它线程中进行的，势必不会立即得到正确的结果
	 * @param orderId
	 * @return
	 */
	Future<String> findOrderDetails(long orderId);
	
	/**提交订单，没有返回值
	 * @param account
	 * @param orderId
	 */
	void order(String account, long orderId);
	
}
