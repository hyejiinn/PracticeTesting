package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

@RequiredArgsConstructor
@Service
public class OrderService
{
	private final ProductRepository productRepository;
	
	private final OrderRepository orderRepository;
	
	public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime)
	{
		// 주문 생성과 관련된 비지니스 로직 담는 메서드
		List<String> productNumbers = request.getProductNumbers();
		
		// 상품 번호의 중복을 고려해 products 리스트를 구함
		List<Product> products = findProductsBy(productNumbers);
		
		// order
		Order order = Order.create(products, registeredDateTime);
		Order savedOrder = orderRepository.save(order);
		
		return OrderResponse.of(savedOrder);
	}
	
	private List<Product> findProductsBy(List<String> productNumbers)
	{
		// product
		List<Product> products = productRepository.findAllByProductNumberIn(
				productNumbers);
		// -> 해당 products는 productNumbers에 같은 상품 번호가 들어오면 알아서 중복 제거를 통해 상품이 하나만 나오게 된다.
		
		// 따라서 productNumber에 맞는 Product를 Map으로 만들어주고
		Map<String, Product> productMap = products.stream()
				.collect(Collectors.toMap(Product::getProductNumber, p -> p));
		
		// 실제 담긴 상품 번호가 담긴 productNumbers를 가지고 map에서 value값을 get에서 이를 다시 List로 만들어준다.
		return productNumbers.stream()
				.map(productMap::get).collect(Collectors.toList());
	}
}
