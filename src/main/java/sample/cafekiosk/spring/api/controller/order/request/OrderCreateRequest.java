package sample.cafekiosk.spring.api.controller.order.request;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderCreateRequest
{
	private List<String> productNumbers; // 상품번호 리스트를 받기로 했음
	
	@Builder
	public OrderCreateRequest(List<String> productNumbers)
	{
		this.productNumbers = productNumbers;
	}
}
