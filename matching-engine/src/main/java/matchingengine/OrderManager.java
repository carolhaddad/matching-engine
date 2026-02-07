package matchingengine;

import java.util.*;

public class OrderManager {

    private long nextId = 1;
    private long nextTime = 1;
    private Map<Long, Order> orders = new HashMap<>();

    public Order createLimit(Order.Type type, Order.Side side, double price, int qty) {
        Order o = new Order(type, side, price, qty, nextId++, nextTime++);
        orders.put(o.getId(), o);
        return o;
    }

    public Order createMarket(Order.Side side, int qty) {
        return new Order(Order.Type.MARKET, side, -1, qty, nextId++, nextTime++);
    }

    public Order get(long id) {
        return orders.get(id);
    }

    public void remove(long id) {
        orders.remove(id);
    }

    public void updateTimePriority(Order o) {
        o.setTimePriority(nextTime++);
    }
}
