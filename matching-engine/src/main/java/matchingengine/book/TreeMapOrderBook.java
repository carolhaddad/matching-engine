package matchingengine.book;

import java.util.*;

import matchingengine.boundary.Price;
import matchingengine.domain.Order;

public class TreeMapOrderBook implements OrderBook {

    private TreeMap<Long, LinkedList<Order>> buys;
    private TreeMap<Long, LinkedList<Order>> sells;

    public TreeMapOrderBook() {
        buys = new TreeMap<>(Collections.reverseOrder());
        sells = new TreeMap<>();
    }

    public void add(Order o) {
        TreeMap<Long, LinkedList<Order>> book = o.getSide() == Order.Side.BUY ? buys : sells;
        book.computeIfAbsent(o.getPrice(), k -> new LinkedList<>()).addLast(o);
    }

    public void remove(Order o) {
        TreeMap<Long, LinkedList<Order>> book = o.getSide() == Order.Side.BUY ? buys : sells;
        LinkedList<Order> level = book.get(o.getPrice());
        if (level != null) {
            level.remove(o);
            if (level.isEmpty()) book.remove(o.getPrice());
        }
    }

    public Order bid() {
        return buys.isEmpty() ? null : buys.firstEntry().getValue().peekFirst();
    }

    public Order ask() {
        return sells.isEmpty() ? null : sells.firstEntry().getValue().peekFirst();
    }

    public long bidPrice() {
        return buys.isEmpty() ? -1 : buys.firstKey();
    }

    public long askPrice() {
        return sells.isEmpty() ? -1 : sells.firstKey();
    }

    public boolean isEmpty(Order.Side side) {
        return side == Order.Side.BUY ? buys.isEmpty() : sells.isEmpty();
    }

    public void printBook() {
        System.out.printf("%-25s | %-25s%n", "Ordens de Compra", "Ordens de Venda");
        System.out.println("--------------------------+---------------------------");

        List<Order> buysList = new ArrayList<>();
        for (LinkedList<Order> level : buys.values()) buysList.addAll(level);

        List<Order> sellsList = new ArrayList<>();
        for (LinkedList<Order> level : sells.values()) sellsList.addAll(level);

        int max = Math.max(buysList.size(), sellsList.size());
        for (int i = 0; i < max; i++) {
            String buy = "", sell = "";
            if (i < buysList.size()) {
                Order o = buysList.get(i);
                buy = String.format("%d @ %.2f", o.getQty(), Price.toDouble(o.getPrice()));
            }
            if (i < sellsList.size()) {
                Order o = sellsList.get(i);
                sell = String.format("%d @ %.2f", o.getQty(), Price.toDouble(o.getPrice()));
            }
            System.out.printf("%-25s | %-25s%n", buy, sell);
        }
    }
}
