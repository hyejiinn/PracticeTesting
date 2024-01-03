package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.List;

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
		
		// product
		List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
		
		// order
		Order order = Order.create(products, registeredDateTime);
		Order savedOrder = orderRepository.save(order);
		
		return OrderResponse.of(savedOrder);
	}
}
