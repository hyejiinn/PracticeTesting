package sample.cafekiosk.spring.api.controller.order.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;

@Getter
@NoArgsConstructor
public class OrderCreateRequest
{
	@NotEmpty(message = "상품 번호 리스트는 필수입니다.")
	private List<String> productNumbers; // 상품번호 리스트를 받기로 했음
	
	@Builder
	public OrderCreateRequest(List<String> productNumbers)
	{
		this.productNumbers = productNumbers;
	}
	
	public OrderCreateServiceRequest toServiceRequest()
	{
		return OrderCreateServiceRequest.builder()
				.productNumbers(productNumbers)
				.build();
	}
}
