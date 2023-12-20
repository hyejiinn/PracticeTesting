package sample.cafekiosk.spring.domain.product;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product extends BaseEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // DB에 ID 값을 생성하는 전략 (보통 IDENTITY 사용한다고함)
	private Long id;
	
	private String productNumber;
	
	@Enumerated(EnumType.STRING)
	private ProductType type;
	
	@Enumerated(EnumType.STRING)
	private ProductSellingStatus sellingStatus;
	
	private String name;
	private int price;
	
	@Builder
	public Product(String productNumber, ProductType type,
			ProductSellingStatus sellingStatus, String name, int price)
	{
		this.productNumber = productNumber;
		this.type = type;
		this.sellingStatus = sellingStatus;
		this.name = name;
		this.price = price;
	}
}
