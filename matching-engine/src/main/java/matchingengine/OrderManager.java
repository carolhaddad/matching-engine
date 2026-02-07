package matchingengine;

import java.util.*;

public class OrderManager {
    
    private long id = 1;
    private long timePriority = 1;

    private Map<Long, Order> orders = new HashMap<>();

    public Order createOrder(Order.Type type, Order.Side side, double price, int qty) {
        long idProx = id++;
        timePriority++;

        Order o = new Order(type, side, price, qty, idProx, timePriority);
        orders.put(id, o);
        return o;
    }

    public Order getOrder(long id) {
        return orders.get(id);
    }

    public void removeOrder(long id) {
        orders.remove(id);
    }
}
