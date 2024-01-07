package sample.cafekiosk.spring.api.controller.product;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.dto.request.ProductCreateRequest;

@RequiredArgsConstructor
@RestController
public class ProductController
{
	private final ProductService productService;

	/**
	 * 새로운 상품 등록
	 */
	@PostMapping("/api/v1/products/new")
	public void createProduct(ProductCreateRequest request) {
		productService.createProduct(request);
	}
	
	/**
	 * 판매 상품 조회
	 */
	@GetMapping("/api/v1/products/selling")
	public List<ProductResponse> getSellingProducts()
	{
		return productService.getSellingProducts();
	}
}
