package matchingengine;

import java.util.*;
import java.util.function.Consumer;

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

    public void updateBid(double bid, Consumer<Order> matcher) {
        pegBuy.sort(Comparator.comparingLong(Order::getTimePriority));

        for (Order o : new ArrayList<>(pegBuy)) {
            book.remove(o);
            o.setPrice(bid);

            int qntyOriginal = o.getQty();

            matcher.accept(o);

            if (o.getQty() == qntyOriginal) {
                book.add(o);
                break;
            }
        }
    }


    public void updateAsk(double ask, Consumer<Order> matchLimit) {
        pegSell.sort(Comparator.comparingLong(Order::getTimePriority));

        for (Order o : new ArrayList<>(pegSell)) {
            book.remove(o);
            o.setPrice(ask);

            int qntyOriginal = o.getQty();

            matchLimit.accept(o);

            if (o.getQty() == qntyOriginal) {
                book.add(o);
                break;
            }
        }

    }
}
