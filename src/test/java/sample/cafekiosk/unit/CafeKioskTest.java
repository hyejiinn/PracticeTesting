package sample.cafekiosk.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 수동 테스트 vs 자동화된 테스트
 */
class CafeKioskTest {

    // 이 테스트가 테스트 코드를 잘 짠 코드인가?
    // 일단 자동화된 테스트인가 ? -> No
    // 이 코드는 무조건 성공하는 테스트를 짠 것..
    // 무얼 검증하고 싶은 코드인가 ?
    @Test
    @DisplayName("수동 테스트")
    void add_manual_test() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println(">> 담긴 음료 수 : " + cafeKiosk.getBeverages().size());
        System.out.println(">> 담긴 음료 : " + cafeKiosk.getBeverages().get(0).getName());
    }

    @Test
//    @DisplayName("자동화된 테스트 : 음료 1개 추가 테스트")
    @DisplayName("음료 1개를 추가하면 주문 목록에 담긴다.")
    void add() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(1);
        assertThat(cafeKiosk.getBeverages()).hasSize(1);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("Americano");
    }

    @Test
    @DisplayName("테스트 케이스 세분화하기 - 해피 케이스 : 경계값 테스트")
    void addSeveralBeverage() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano, 2);

        assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(americano);
        assertThat(cafeKiosk.getBeverages().get(1)).isEqualTo(americano);
    }

    @Test
    @DisplayName("테스트 케이스 세분화하기 - 예외 케이스 : 경계값 테스트")
    void addZeroBeverage() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 1잔 이상 주문할 수 있습니다.");
    }

    @Test
    @DisplayName("카페 키오스크 삭제 테스트 ")
    void remove() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);
        assertThat(cafeKiosk.getBeverages()).hasSize(1);

        cafeKiosk.remove(americano);
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    @DisplayName("카페 키오스크 전체 삭제 테스트 ")
    void clear() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);
        assertThat(cafeKiosk.getBeverages()).hasSize(2);

        cafeKiosk.clear();
        assertThat(cafeKiosk.getBeverages()).isEmpty();

    }
    
//    @DisplayName("TDD로 calculateTotalPrice 메서드 작성해보기 ")
    @Test
    @DisplayName("주문 목록에 담긴 상품들의 총 금액을 계산할 수 있다.")
    void calculateTotalPrice()
    {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();
    
        cafeKiosk.add(americano);
        cafeKiosk.add(latte);
        
        // when
        int totalPrice = cafeKiosk.calculateTotalPriceTdd();
    
        // then
        assertThat(totalPrice).isEqualTo(8500);
    }
    

    @Test
    @DisplayName("주문 시간 내에 주문할 경우 테스트가 통과, 하지만 주문 시간 외에 테스트하면 테스트 실패함 ")
    void createOrder() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        Order order = cafeKiosk.createOrder();

        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("Americano");
    }

    @Test
    @DisplayName("직접 시간을 지정해서 테스트 수행 ")
    void createOrderWithCurrentTiime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        Order order = cafeKiosk.createOrder(LocalDateTime.of(2023, 12, 13, 10, 0));

        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("Americano");
    }

    @Test
    @DisplayName("직접 시간을 지정해서 예외 테스트 수행 ")
    void createOrderOutsideOpenTime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        assertThatThrownBy(() -> {
            Order order = cafeKiosk.createOrder(LocalDateTime.of(2023, 12, 13, 9, 59));
        }).isInstanceOf(IllegalArgumentException.class).hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요.");

    }

}





