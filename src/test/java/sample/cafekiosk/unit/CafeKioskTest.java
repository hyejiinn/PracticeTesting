package sample.cafekiosk.unit;

import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;

import static org.junit.jupiter.api.Assertions.*;

class CafeKioskTest {

    // 이 테스트가 테스트 코드를 잘 짠 코드인가?
    // 일단 자동화된 테스트인가 ? -> No
    // 이 코드는 무조건 성공하는 테스트를 짠 것..
    // 무얼 검증하고 싶은 코드인가 ?
    @Test
    void add() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println(">> 담긴 음료 수 : " + cafeKiosk.getBeverages().size());
        System.out.println(">> 담긴 음료 : " + cafeKiosk.getBeverages().get(0).getName());
    }

}