package matchingengine;

import java.util.*;

public class MatchingEngine {

    private final PriorityQueueOrderBook book;
    private final OrderManager manager;
    private final PegManager pegManager;
    private final List<Trade> trades;

    private double lastBid = -1;
    private double lastAsk = -1;

    public MatchingEngine() {
        this.book = new PriorityQueueOrderBook();
        this.manager = new OrderManager();
        this.pegManager = new PegManager();
        this.trades = new ArrayList<>();
    }

    public void newOrder (Order.Type type, Order.Side side, double price, int qty) {
        Order order = manager.createOrder(type, side, price, qty);

        if (type == Order.Type.PEG) {
            pegManager.add(order);
            book.add(order);
            return;
        }

        match(order);

        if (type == Order.Type.LIMIT) {
            book.add(order);
            updatePeg();
        }
    }

    private void match (Order order) {
        if (order.getSide() == Order.Side.BUY) {
            while (order.getQty() > 0 &&  book.ask().getPrice() <= order.getPrice()) {
                Order ask = book.ask();
                trade(order, ask);
            }
        } 
        else {
            while (order.getQty() > 0 && book.bid().getPrice() >= order.getPrice()) {
                Order bid = book.bid();
                trade(bid, order);
            }
        }
    }

    private void trade (Order buy, Order sell) {
        int tradeQty = Math.min(buy.getQty(), sell.getQty());
        double tradePrice = sell.getPrice();

        buy.setQty(buy.getQty() - tradeQty);
        sell.setQty(sell.getQty() - tradeQty);

        Trade t = new Trade(tradePrice, tradeQty, buy.getId(), sell.getId());
        trades.add(t);

        System.out.println("Trade, price: " + tradePrice + ", qty: " + tradeQty);

        book.remove(buy);
        book.remove(sell);

        if (sell.getQty() > 0) book.add(sell);
        if (buy.getQty() > 0) book.add(buy);
    }

    public void cancelOrder(long id) {
        Order o = manager.getOrder(id);
        if (o == null) return;

        book.remove(o);
        pegManager.remove(o);
        manager.removeOrder(id);

        System.out.println("Order cancelled");
    }

    public void updateOrder(long id, double newPrice, int newQty) {
        Order o = manager.getOrder(id);
        if (o == null) return;

        book.remove(o);

        o.setPrice(newPrice);
        o.setQty(newQty);
        book.add(o);

        System.out.println("Order modified");
    }

    public void printBook() {
        book.printBook();
    }

    private void updatePeg() {
        double bid = book.bid().getPrice();
        double ask = book.ask().getPrice();

        if (bid != lastBid) {
            pegManager.update(bid, 0.0);
            lastBid = bid;
        }

        if (ask != lastAsk) {
            pegManager.update(0.0, ask);
            lastAsk = ask;
        }
    }

    public List<Trade> getTrades() {
        return trades;
    }
}

