package matchingengine;

import java.util.*;

public class PriorityQueueOrderBook implements OrderBook {

    private PriorityQueue<Order> buys;
    private PriorityQueue<Order> sells;

    public PriorityQueueOrderBook() {
        buys = new PriorityQueue<>((a, b) -> {
            if (a.getPrice() != b.getPrice())
                return Double.compare(b.getPrice(), a.getPrice());
            return Long.compare(a.getTimePriority(), b.getTimePriority());
        });

        sells = new PriorityQueue<>((a, b) -> {
            if (a.getPrice() != b.getPrice())
                return Double.compare(a.getPrice(), b.getPrice());
            return Long.compare(a.getTimePriority(), b.getTimePriority());
        });
    }

    public void add(Order o) {
        if (o.getSide() == Order.Side.BUY) buys.add(o);
        else sells.add(o);
    }

    public void remove(Order o) {
        if (o.getSide() == Order.Side.BUY) buys.remove(o);
        else sells.remove(o);
    }

    public Order bid() { return buys.peek(); }
    public Order ask() { return sells.peek(); }

    public double bidPrice() {
        return buys.isEmpty() ? -1 : buys.peek().getPrice();
    }

    public double askPrice() {
        return sells.isEmpty() ? -1 : sells.peek().getPrice();
    }

    public boolean isEmpty(Order.Side side) {
        return side == Order.Side.BUY ? buys.isEmpty() : sells.isEmpty();
    }

    public void printBook() {
        List<Order> buysList = new ArrayList<>(buys);
        List<Order> sellsList = new ArrayList<>(sells);

        buysList.sort((a, b) -> {
            if (a.getPrice() != b.getPrice()){
                return Double.compare(b.getPrice(), a.getPrice());
            }
            return Long.compare(a.getTimePriority(), b.getTimePriority());
        });

       sellsList.sort((a, b) -> {
            if (a.getPrice() != b.getPrice()) {
                return Double.compare(a.getPrice(), b.getPrice());
            }
            return Long.compare(a.getTimePriority(), b.getTimePriority());
        });

        System.out.printf("%-25s | %-25s %n", "Ordens de Compra", "Ordens de Venda");
        System.out.println("---------------------------+---------------------------");

        int max = Math.max(buysList.size(), sellsList.size());

        for (int i = 0; i < max; i++) {
            String buy = "";
            String sell = "";

            if (i < buysList.size()) {
                Order buyO = buysList.get(i);
                buy = String.format("%d @ %.2f", buyO.getQty(), buyO.getPrice());
            }
            if (i < sellsList.size()) {
                Order sellO = sellsList.get(i);
                sell = String.format("%d @ %.2f", sellO.getQty(), sellO.getPrice());
            }

            System.out.printf("%-25s | %-25s%n", buy, sell);
        }
    }
}
