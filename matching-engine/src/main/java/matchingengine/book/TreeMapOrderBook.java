package matchingengine.book;

import java.util.*;

import matchingengine.domain.Order;
import matchingengine.dto.BookSnapshot;

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


public BookSnapshot printBook() {
    List<BookSnapshot.Level> buyList = new ArrayList<>();
    List<BookSnapshot.Level> sellList = new ArrayList<>();

    // BUY side (preço desc, tempo asc)
    for (Map.Entry<Long, PriorityQueue<Order>> entry : buys.entrySet()) {
        List<Order> level = new ArrayList<>(entry.getValue());
        level.sort(Comparator.comparingLong(Order::getTimePriority));

        for (Order o : level) {
            buyList.add(new BookSnapshot.Level(o.getPrice(), o.getQty()));
        }
    }

    // SELL side (preço asc, tempo asc)
    for (Map.Entry<Long, PriorityQueue<Order>> entry : sells.entrySet()) {
        List<Order> level = new ArrayList<>(entry.getValue());
        level.sort(Comparator.comparingLong(Order::getTimePriority));

        for (Order o : level) {
            sellList.add(new BookSnapshot.Level(o.getPrice(), o.getQty()));
        }
    }

    return new BookSnapshot(buyList, sellList);
}


}