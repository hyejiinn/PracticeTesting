package sample.cafekiosk.spring.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;
import sample.cafekiosk.spring.domain.orderProduct.OrderProduct;
import sample.cafekiosk.spring.domain.product.Product;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders") // SQL에서 ORDER는 예약어이기 때문에 실제 테이블 이름은 orders로 설정해준다.
@Entity
public class Order extends BaseEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	private int totalPrice;
	
	private LocalDateTime registeredDateTime; // 주문 시간
	
	// OrderProduct에 있는 필드명을 선언해주는 것이다.
	// OrderProduct는 Order가 삭제될 때 마다 같이 삭제될 수 있도록 CascadeType을 All로 지정했다.
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderProduct> orderProducts = new ArrayList<>(); // 양방향 관계
	
	public Order(List<Product> products, LocalDateTime registeredDateTime)
	{
		this.orderStatus = OrderStatus.INIT;
		this.totalPrice = calculateTotalPrice(products);
		this.registeredDateTime = registeredDateTime;
		this.orderProducts = products.stream().map(product -> new OrderProduct(this, product)).collect(Collectors.toList());
		
	}
	
	private int calculateTotalPrice(List<Product> products)
	{
		return products.stream().mapToInt(Product::getPrice).sum();
	}
	
	public static Order create(List<Product> products, LocalDateTime registeredDateTime)
	{
		return new Order(products, registeredDateTime);
	}
}
