package sample.cafekiosk.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;

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
    @DisplayName("자동화된 테스트")
    void add() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(1);
        assertThat(cafeKiosk.getBeverages()).hasSize(1);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("Americano");
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

}