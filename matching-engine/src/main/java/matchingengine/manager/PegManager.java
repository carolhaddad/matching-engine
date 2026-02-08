package matchingengine.manager;

import java.util.*;

import matchingengine.book.OrderBook;
import matchingengine.domain.Order;

public class PegManager {

    private List<Order> pegBuy = new ArrayList<>();
    private List<Order> pegSell = new ArrayList<>();
    private OrderBook book;

    public PegManager(OrderBook book) {
        this.book = book;
    }

    public void add(Order o) {
        if (o.getSide() == Order.Side.BUY) pegBuy.add(o);
        else pegSell.add(o);
    }

    public void remove(Order o) {
        if (o.getSide() == Order.Side.BUY) pegBuy.remove(o);
        else pegSell.remove(o);
    }

    public void updateBid(long bid) { 
        for (Order o : new ArrayList<>(pegBuy)) {
            book.remove(o);
            o.setPrice(bid);
            book.add(o);
        }
    }

    public void updateAsk(long ask) {
        for (Order o : new ArrayList<>(pegSell)) {
            book.remove(o);
            o.setPrice(ask);
            book.add(o);
        }
    }
}
