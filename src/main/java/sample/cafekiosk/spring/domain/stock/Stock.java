package sample.cafekiosk.spring.domain.stock;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Stock extends BaseEntity
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	private String productNumber;
	
	private int quantity; // 재고 수량
	
	@Builder
	public Stock(Long id, String productNumber, int quantity)
	{
		this.id = id;
		this.productNumber = productNumber;
		this.quantity = quantity;
	}
	
	public static Stock create(String productNumber, int quantity)
	{
		return Stock.builder()
				.productNumber(productNumber)
				.quantity(quantity)
				.build();
	}
	
	public boolean isQuantityLessThan(int quantity)
	{
		return this.quantity < quantity;
	}
	
	public void deductQuantity(int quantity)
	{
		if (isQuantityLessThan(quantity)) {
			throw new IllegalArgumentException("차감할 재고 수량이 없습니다.");
		}
		this.quantity -= quantity;
	}
}
