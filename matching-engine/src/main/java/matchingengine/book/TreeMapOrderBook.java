package matchingengine.book;

import java.util.*;

import matchingengine.boundary.Price;
import matchingengine.domain.Order;

public class TreeMapOrderBook implements OrderBook {

    private TreeMap<Long, PriorityQueue<Order>> buys;
    private TreeMap<Long, PriorityQueue<Order>> sells;

    public TreeMapOrderBook() {
        buys = new TreeMap<>(Collections.reverseOrder());
        sells = new TreeMap<>();
    }

    private Comparator<Order> timeComparator() {
        return Comparator.comparingLong(Order::getTimePriority);
    }

       public void add(Order o) {
        TreeMap<Long, PriorityQueue<Order>> book = o.getSide() == Order.Side.BUY ? buys : sells;
        book.computeIfAbsent(o.getPrice(), k -> new PriorityQueue<>(timeComparator())).add(o);
    }

    public void remove(Order o) {
        TreeMap<Long, PriorityQueue<Order>> book = o.getSide() == Order.Side.BUY ? buys : sells;
        PriorityQueue<Order> level = book.get(o.getPrice());
        if (level != null) {
            level.remove(o);
            if (level.isEmpty()) book.remove(o.getPrice());
        }
    }

    public Order bid() {
        return buys.isEmpty() ? null : buys.firstEntry().getValue().peek();
    }

    public Order ask() {
        return sells.isEmpty() ? null : sells.firstEntry().getValue().peek();
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

    // iteração por nível de preço (já ordenado pelo TreeMap)
    Iterator<Map.Entry<Long, PriorityQueue<Order>>> buyIt = buys.entrySet().iterator();
    Iterator<Map.Entry<Long, PriorityQueue<Order>>> sellIt = sells.entrySet().iterator();

    List<Order> buyLevel = buyIt.hasNext() ? new ArrayList<>(buyIt.next().getValue()) : new ArrayList<>();
    List<Order> sellLevel = sellIt.hasNext() ? new ArrayList<>(sellIt.next().getValue()) : new ArrayList<>();

    // ordenar cada nível pelo timePriority
    buyLevel.sort(Comparator.comparingLong(Order::getTimePriority));
    sellLevel.sort(Comparator.comparingLong(Order::getTimePriority));

    int buyIndex = 0, sellIndex = 0;
    while (buyIt.hasNext() || buyIndex < buyLevel.size() || sellIt.hasNext() || sellIndex < sellLevel.size()) {
        String buy = "", sell = "";

        // imprimir próximo da lista de compra
        if (buyIndex < buyLevel.size()) {
            Order o = buyLevel.get(buyIndex++);
            buy = String.format("%d @ %.2f", o.getQty(), Price.toDouble(o.getPrice()));
        } else if (buyIt.hasNext()) {
            buyLevel = new ArrayList<>(buyIt.next().getValue());
            buyLevel.sort(Comparator.comparingLong(Order::getTimePriority));
            buyIndex = 0;
            if (!buyLevel.isEmpty()) {
                Order o = buyLevel.get(buyIndex++);
                buy = String.format("%d @ %.2f", o.getQty(), Price.toDouble(o.getPrice()));
            }
        }

        // imprimir próximo da lista de venda
        if (sellIndex < sellLevel.size()) {
            Order o = sellLevel.get(sellIndex++);
            sell = String.format("%d @ %.2f", o.getQty(), Price.toDouble(o.getPrice()));
        } else if (sellIt.hasNext()) {
            sellLevel = new ArrayList<>(sellIt.next().getValue());
            sellLevel.sort(Comparator.comparingLong(Order::getTimePriority));
            sellIndex = 0;
            if (!sellLevel.isEmpty()) {
                Order o = sellLevel.get(sellIndex++);
                sell = String.format("%d @ %.2f", o.getQty(), Price.toDouble(o.getPrice()));
            }
        }

        System.out.printf("%-25s | %-25s%n", buy, sell);
    }
}
}