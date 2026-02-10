package matchingengine.book;

import matchingengine.domain.Order;
import matchingengine.dto.BookSnapshot;

public interface OrderBook {
    void add(Order o);
    void remove(Order o);
    Order bid();
    Order ask();
    BookSnapshot printBook();
    long bidPrice();
    long askPrice();
    boolean isEmpty(Order.Side side);
}