package sample.cafekiosk.unit.beverage;

import sample.cafekiosk.unit.beverage.Beverage;

public class Latte implements Beverage {
    @Override
    public String getName() {
        return "latte";
    }

    @Override
    public int getPrice() {
        return 4500;
    }
}
