package sample.cafekiosk.spring.domain.product.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor // ObjectMapper가 직렬화/역직렬화 기능을 수행할 때 기본 생성자를 통해 만들기 때문에 필요
public class ProductCreateRequest {

    @NotNull(message = "상품 타입은 필수입니다.")
    private ProductType type;
    
    @NotNull(message = "상품 판매상태는 필수입니다.")
    private ProductSellingStatus sellingStatus;
    
    /**
     * @NotNull : "" or " " 통과
     * @NotEmpty : "      " 통과 (공백 통과)
     * @NotBlank : "" or " " 통과 x
     * -> String에서 주로 공백을 통과시키는 경우는 거의 없기 때문에 @NotBlank를 사용하자.
     */
    // 도메인 정책으로 "상품 이름은 20자 제한" 으로 하고 싶다면? 검증은 어디서 해야할까 -> 고민이 필요한 포인트
    @NotBlank(message = "상품 이름은 필수입니다.")
//    @Max(20) //  도메인 성격에 따른 특수한 정책에 대해서도 과연 컨트롤러 단계에서 튕겨내는게 맞는가? => 강사님은 기본적인 @NotBlank만 ! 더 안쪽 레이어에서 검증하는게 맞다고 생각한다고 하심
    private String name;
    
    @Positive(message = "상품 가격은 0원 이상이어야 합니다.")
    private int price;

    @Builder
    public ProductCreateRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }
    
    public ProductCreateServiceRequest toServiceRequest()
    {
        return ProductCreateServiceRequest.builder()
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }
}
