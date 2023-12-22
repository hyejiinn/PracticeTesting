package sample.cafekiosk.spring.domain.product;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

/**
 * Repository에 대한 test를 진행하는 이유
 * - 지금은 쿼리 메서드가 간단하지만 where절에 조건이 엄청 붙어서 길어지면 이를 테스트하는 과정이 필요하다.
 * - 그리고 현재는 이렇지만 나중에 어떻게 변할지 모르기 때문에 테스트는 해야 한다 !
 */
// Spring 서버를 띄워서 테스트를 하는데 스프링 부트 테스트보다 가벼운 편이다. (속도가 좀 더 빠르다.)
@ActiveProfiles("test")
//@DataJpaTest // 강사님은 SpringBootTest를 좀 더 선호한다고는 함 ㅋㅋ
@SpringBootTest // Spring 서버를 띄워서 테스트
class ProductRepositoryTest
{
	@Autowired
	private ProductRepository productRepository;
	
	
	@DisplayName("원하는 판매상태를 가진 상품들을 조회한다. ")
	@Test
	void findAllBySellingStatusIn() {
	    // given : 테스트를 위한 모든 재료
		Product product1 = Product.builder()
				.productNumber("001")
				.type(HANDMADE)
				.sellingStatus(SELLING)
				.name("아메리카노")
				.price(4000)
				.build();
		Product product2 = Product.builder()
				.productNumber("002")
				.type(HANDMADE)
				.sellingStatus(HOLD)
				.name("카페라떼")
				.price(4500)
				.build();
		Product product3 = Product.builder()
				.productNumber("001")
				.type(HANDMADE)
				.sellingStatus(STOP_SELLING)
				.name("팥빙수")
				.price(7000)
				.build();
		productRepository.saveAll(List.of(product1, product2, product3)); // JPA 기본 제공 메서드 saveAll
		
		// when
		List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));
		
		
		// then
		assertThat(products).hasSize(2)
				.extracting("productNumber", "name",
							"sellingStatus")// 검증하고자 하는 것만 추출해서 테스트 가능
				.containsExactlyInAnyOrder(tuple("001", "아메리카노", SELLING), Tuple.tuple("002", "카페라떼", HOLD)
						);
	}
}