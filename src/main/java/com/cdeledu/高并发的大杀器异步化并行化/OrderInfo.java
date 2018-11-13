package com.cdeledu.高并发的大杀器异步化并行化;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {

	public OrderInfo(CustomerInfo customerInfo, TenantInfo tenantInfo, DiscountInfo discountInfo, FoodListInfo foodListInfo, OtherInfo otherInfo) {
		this.customerInfo = customerInfo;
		this.tenantInfo = tenantInfo;
		this.discountInfo = discountInfo;
		this.foodListInfo = foodListInfo;
		this.otherInfo = otherInfo;
	}
	private Integer orderId;
	private CustomerInfo customerInfo;
	private DiscountInfo discountInfo;
	private FoodListInfo foodListInfo;
	private TenantInfo tenantInfo;
	private OtherInfo otherInfo;

}
