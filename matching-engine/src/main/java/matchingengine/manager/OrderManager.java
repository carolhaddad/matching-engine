//gera IDs únicos e gerencia prioridade temporal

package matchingengine.manager;

import java.util.*;

import matchingengine.domain.Order;

public class OrderManager {

    private long id;
    private long time;
    private Map<Long, Order> orders;

    public OrderManager() {
        this.id = 1;
        this.time = 1;
        this.orders = new HashMap<>();
    }
    
    public Order createLimit(Order.Type type, Order.Side side, long price, int qty) {
        Order o = new Order(type, side, price, qty, id++, time++);
        orders.put(o.getId(), o);
        return o;
    }

    public Order createMarket(Order.Side side, int qty) {
        return new Order(Order.Type.MARKET, side, -1, qty, id++, time++);
    }

    public Order get(long id) {
        return orders.get(id);
    }

    public void remove(long id) {
        orders.remove(id);
    }

    //atualiza prioridade temporal após modify 
    public void updateTimePriority(Order o) { 
        o.setTimePriority(time++);
    }
}
