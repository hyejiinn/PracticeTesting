package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderService
{
	private final ProductRepository productRepository;
	
	private final OrderRepository orderRepository;
	
	private final StockRepository stockRepository;
	
	/**
	 * 재고 감소 -> 동시성 고민
	 * 키오스크가 두대 이상인데 동시에 주문을 하면..?
	 * 보통 -> optimistic lock / pessimistic lock/ ... 해결...
	 */
	public OrderResponse createOrder(OrderCreateServiceRequest request, LocalDateTime registeredDateTime)
	{
		// 주문 생성과 관련된 비지니스 로직 담는 메서드
		List<String> productNumbers = request.getProductNumbers();
		
		// 상품 번호의 중복을 고려해 products 리스트를 구함
		List<Product> products = findProductsBy(productNumbers);
		
		// 상품 재고 확인 및 재고 차감
		deductStockQuantities(products);
		
		// order
		Order order = Order.create(products, registeredDateTime);
		Order savedOrder = orderRepository.save(order);
		
		return OrderResponse.of(savedOrder);
	}
	
	private void deductStockQuantities(List<Product> products)
	{
		// 1. 재고 차감 체크가 필요한 상품들 필터링 -> 재고 차감이 필요한 상품 번호들만 남을 것
		List<String> stockProductNumbers = extractStockProductNumbers(products);
		
		// 2. 재고 엔티티 조회
		Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);
		
		// 3. 상품별 counting
		Map<String, Long> productCountingMap = countingMapBy(stockProductNumbers);
		
		// 4. 재고 차감 시도
		for (String stockProductNumber : new HashSet<>(stockProductNumbers))
		{
			Stock stock = stockMap.get(stockProductNumber);
			int quantity = productCountingMap.get(stockProductNumber).intValue();
			
			if (stock.isQuantityLessThan(quantity)) {
				throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
			}
			stock.deductQuantity(quantity);
		}
	}
	private static List<String> extractStockProductNumbers(List<Product> products)
	{
		return products.stream()
				.filter(product -> ProductType.containsStockType(product.getType()))
				.map(Product::getProductNumber)
				.collect(Collectors.toList());
	}
	private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers)
	{
		List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
		
		return stocks.stream()
				.collect(Collectors.toMap(Stock::getProductNumber, s -> s));
	}
	
	private static Map<String, Long> countingMapBy(List<String> stockProductNumbers)
	{
		return stockProductNumbers.stream()
				.collect(Collectors.groupingBy(p -> p, Collectors.counting()));
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
