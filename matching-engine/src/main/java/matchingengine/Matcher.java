package matchingengine;

import java.util.*;

public class Matcher {

    private OrderBook book;
    private OrderManager manager;
    private PegManager pegManager;

    private double lastBid = -1;
    private double lastAsk = -1;

    public Matcher(OrderBook book, OrderManager manager, PegManager pegManager) {
        this.book = book;
        this.manager = manager;
        this.pegManager = pegManager;
    }

    public List<Trade> matchLimit(Order order) {
        List<Trade> trades = new ArrayList<>();

        if (order.getSide() == Order.Side.BUY) {
            while (order.getQty() > 0 && !book.isEmpty(Order.Side.SELL)
                    && book.askPrice() <= order.getPrice()) {
                trades.add(executeTrade(order, book.ask()));
            }
        } else {
            while (order.getQty() > 0 && !book.isEmpty(Order.Side.BUY)
                    && book.bidPrice() >= order.getPrice()) {
                trades.add(executeTrade(book.bid(), order)); // OBS: "inverter" se usarmos regra do "preço do livro"
            }
        }

        if (order.getQty() > 0) updatePeg();
        
        return trades;
    }

    public List<Trade> matchMarket(Order order) {
        List<Trade> trades = new ArrayList<>();

        if (order.getSide() == Order.Side.BUY) {
            while (order.getQty() > 0 && !book.isEmpty(Order.Side.SELL)) {
                trades.add(executeTrade(order, book.ask()));
            }
        } else {
            while (order.getQty() > 0 && !book.isEmpty(Order.Side.BUY)) {
                trades.add(executeTrade(book.bid(), order));
            }
        }

        return trades;
    }

    private Trade executeTrade(Order buy, Order sell) {
        int qty = Math.min(buy.getQty(), sell.getQty());
        double price = sell.getPrice();

        buy.setQty(buy.getQty() - qty);
        sell.setQty(sell.getQty() - qty);


        if (buy.getQty() == 0) {
            book.remove(buy);
            manager.remove(buy.getId());
            pegManager.remove(buy);
        }

        if (sell.getQty() == 0) {
            book.remove(sell);
            manager.remove(sell.getId());
            pegManager.remove(sell);
        }
        
        return new Trade(price, qty, buy.getId(), sell.getId());
    }


    private void updatePeg() {
        double bid = book.isEmpty(Order.Side.BUY) ? -1 : book.bidPrice();
        double ask = book.isEmpty(Order.Side.SELL) ? -1 : book.askPrice();

        if (bid != lastBid) {
            pegManager.updateBid(bid);
            lastBid = bid;
        }

        if (ask != lastAsk) {
            pegManager.updateAsk(ask);
            lastAsk = ask;
        }
    }
}

