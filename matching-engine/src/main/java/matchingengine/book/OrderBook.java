package matchingengine.book;

import matchingengine.domain.Order;

public interface OrderBook {
    void add(Order o);
    void remove(Order o);
    Order bid();
    Order ask();
    void printBook();
    long bidPrice();
    long askPrice();
    boolean isEmpty(Order.Side side);
}