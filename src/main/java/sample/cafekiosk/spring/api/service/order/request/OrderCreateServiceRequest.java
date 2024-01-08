package sample.cafekiosk.spring.api.service.order.request;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Controller의 OrderCreateRequest 분리해서 사용하기
 * -> 이렇게 하는 이유는 Service는 상위의 컨트롤러를 알지 못하게 하기 위해서이다.
 * -> 좀 귀찮더라도 나중에 확장성을 고려했을 때, 나중에 변경이 되더라도 전혀 영향을 받지 않도록
 * -> 의존성과 책임감의 측면에서 좋다.
 */
@Getter
@NoArgsConstructor
public class OrderCreateServiceRequest
{
	private List<String> productNumbers; // 상품번호 리스트를 받기로 했음
	
	@Builder
	public OrderCreateServiceRequest(List<String> productNumbers)
	{
		this.productNumbers = productNumbers;
	}
}
