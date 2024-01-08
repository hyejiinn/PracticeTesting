package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.orderProduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import sample.cafekiosk.spring.domain.product.ProductType;
import static sample.cafekiosk.spring.domain.product.ProductType.BAKERY;
import static sample.cafekiosk.spring.domain.product.ProductType.BOTTLE;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

@ActiveProfiles("test")
//@Transactional // @Transactional 어노테이션은 잘 알고 써야 한다.
@SpringBootTest
//@DataJpaTest
class OrderServiceTest
{
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderProductRepository orderProductRepository;
	
	@Autowired
	private StockRepository stockRepository;
	
	@Autowired
	private OrderService orderService;
	
	@AfterEach
	void tearDown() {
		// 수동 삭제
		orderProductRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		stockRepository.deleteAllInBatch();
	}
	
	@DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
	@Test
	void createOrder() {
	    // given
		// 1. 상품들이 저장되어 있어야 한다.
		// given : 테스트를 위한 모든 재료
		Product product1 = createProduct(HANDMADE, "001", 1000);
		Product product2 = createProduct(HANDMADE, "002", 3000);
		Product product3 = createProduct(HANDMADE, "003", 5000);
		productRepository.saveAll(List.of(product1, product2, product3));
		
		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
				.productNumbers(List.of("001", "002")).build();
		
	    // when
		LocalDateTime registeredDateTime = LocalDateTime.now();
		OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);
		
	    // then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse).extracting("registeredDateTime", "totalPrice")
				.contains(registeredDateTime, 4000);
		
		assertThat(orderResponse.getProducts()).hasSize(2)
				.extracting("productNumber", "price")
				.containsExactlyInAnyOrder(
						tuple("001", 1000),
						tuple("002", 3000)
				);
		
	}
	
	
	@DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
	@Test
	void createOrderWithDuplicateProductNumbers() {
	    // given
		Product product1 = createProduct(HANDMADE, "001", 1000);
		Product product2 = createProduct(HANDMADE, "002", 3000);
		Product product3 = createProduct(HANDMADE, "003", 5000);
		productRepository.saveAll(List.of(product1, product2, product3));
		
		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
				.productNumbers(List.of("001", "001")).build(); // 001 상품을 2개 구매
		
	    // when
		LocalDateTime registeredDateTime = LocalDateTime.now();
		OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);
		
		// then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse).extracting("registeredDateTime", "totalPrice")
				.contains(registeredDateTime, 2000);
		
		assertThat(orderResponse.getProducts()).hasSize(2)
				.extracting("productNumber", "price")
				.containsExactlyInAnyOrder(
						tuple("001", 1000),
						tuple("001", 1000)
				);
	}
	
	@DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
	@Test
	void createOrderWithStock() {
		// given
		// 1. 상품들이 저장되어 있어야 한다.
		// given : 테스트를 위한 모든 재료
		Product product1 = createProduct(BOTTLE, "001", 1000);
		Product product2 = createProduct(BAKERY, "002", 3000);
		Product product3 = createProduct(HANDMADE, "003", 5000);
		productRepository.saveAll(List.of(product1, product2, product3));
		
		Stock stock1 = Stock.create("001", 2);
		Stock stock2 = Stock.create("002", 2);
		stockRepository.saveAll(List.of(stock1, stock2));
		
		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
				.productNumbers(List.of("001", "001", "002", "003")).build();
		
		// when
		LocalDateTime registeredDateTime = LocalDateTime.now();
		OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);
		
		// then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse).extracting("registeredDateTime", "totalPrice")
				.contains(registeredDateTime, 10000);
		
		assertThat(orderResponse.getProducts()).hasSize(4)
				.extracting("productNumber", "price")
				.containsExactlyInAnyOrder(
						tuple("001", 1000),
						tuple("001", 1000),
						tuple("002", 3000),
						tuple("003", 5000)
				);
		
		List<Stock> stocks = stockRepository.findAll();
		assertThat(stocks).hasSize(2)
				.extracting("productNumber", "quantity")
				.containsExactlyInAnyOrder(
						Tuple.tuple("001", 0),
						Tuple.tuple("002", 1)
				);
		
	}
	
	@DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
	@Test
	void createOrderWithNoStock() {
		// given
		// 1. 상품들이 저장되어 있어야 한다.
		// given : 테스트를 위한 모든 재료
		LocalDateTime registeredDateTime = LocalDateTime.now();
		Product product1 = createProduct(BOTTLE, "001", 1000);
		Product product2 = createProduct(BAKERY, "002", 3000);
		Product product3 = createProduct(HANDMADE, "003", 5000);
		productRepository.saveAll(List.of(product1, product2, product3));
		
		Stock stock1 = Stock.create("001", 2);
		Stock stock2 = Stock.create("002", 2);
		stock1.deductQuantity(1); // TODO (이렇게 작성하면 안된다~ 라는 상황을 말하기 위해 잠시 써둠)
		stockRepository.saveAll(List.of(stock1, stock2));
		
		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
				.productNumbers(List.of("001", "001", "002", "003")).build();
		
		// when // then
		assertThatThrownBy(
				() -> orderService.createOrder(request, registeredDateTime))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("재고가 부족한 상품이 있습니다.");
	}
	
	
	
	private Product createProduct(ProductType type, String productNumber, int price)
	{
		return Product.builder()
				.type(type)
				.productNumber(productNumber)
				.price(price)
				.sellingStatus(SELLING)
				.name("메뉴 이름")
				.build();
	}
}