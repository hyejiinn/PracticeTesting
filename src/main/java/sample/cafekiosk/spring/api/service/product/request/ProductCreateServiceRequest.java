package sample.cafekiosk.spring.api.service.product.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor // ObjectMapper가 직렬화/역직렬화 기능을 수행할 때 기본 생성자를 통해 만들기 때문에 필요
public class ProductCreateServiceRequest
{

    private ProductType type;
    
    private ProductSellingStatus sellingStatus;
    
    private String name;
    
    private int price;

    @Builder
    public ProductCreateServiceRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public Product toEntity(String nextProductNumber) {
        return Product.builder()
                .productNumber(nextProductNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }
}
