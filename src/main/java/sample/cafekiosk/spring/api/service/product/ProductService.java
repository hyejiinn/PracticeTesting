package sample.cafekiosk.spring.api.service.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.dto.request.ProductCreateRequest;

/**
 * readOnly  = ture : 읽기전용 트랜잭션 -> CRUD 에서 CUD 동작 X, Only Read
 * JPA 관점 : CUD 스냅샷 저장, 변경감지 X -> 성능향상
 *
 * CQRS - Command / Query
 * 보통의 서비스에 경우 Read 작업이 훨씬 많은 편이다.
 * Command 와 Query 책임을 분리해 서로 연관이 없게 하자.
 *
 */
@Transactional(readOnly = true) // 상단에는 readOnly True를 걸고
@Service
@RequiredArgsConstructor
public class ProductService
{
	private final ProductRepository productRepository;

	// 동시성 이슈..! -> 동시에 여러명이 상품을 등록한다면..?
	// 아니면 상품번호를 DB에셔 Unique index 같은걸로 잡아서 동시에 했다면 한명은 튕겨서 상품 등록을 재시도하게 하던가..
	// 아니면 3번 정도 더 요청을 해보던가 하게 할 수 있다.
	// 정책을 바꿔 상품 번호를 UUID 를 활용해도 된다.
	// CUD 기능이 있다면 @Transactional로 붙여준다.
	@Transactional
	public ProductResponse createProduct(ProductCreateRequest request) {

		String nextProductNumber = createNextProductNumber();

		Product product = request.toEntity(nextProductNumber);
		Product saveProduct = productRepository.save(product);

		return ProductResponse.of(saveProduct);

	}

	public List<ProductResponse> getSellingProducts()
	{
		List<Product> products = productRepository.findAllBySellingStatusIn(
				ProductSellingStatus.forDisplay());
		
		return products.stream().map(ProductResponse::of).collect(Collectors.toList());
	}

	private String createNextProductNumber() {
		// 1. productNumber 부여 : DB에서 마지막 저장된 Product의 상품 번호를 읽어와서 +1
		String latestProductNumber = productRepository.findLatestProductNumber();
		if (latestProductNumber == null) {
			return "001";
		}

		int latestProductNumberInt = Integer.valueOf(latestProductNumber);
		int nextProductNumberInt = latestProductNumberInt + 1;

		return String.format("%03d", nextProductNumberInt);
	}

}
