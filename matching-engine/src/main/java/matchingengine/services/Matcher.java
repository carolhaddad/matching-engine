package matchingengine.services;

import java.util.*;

import matchingengine.book.OrderBook;
import matchingengine.domain.Order;
import matchingengine.manager.OrderManager;
import matchingengine.manager.PegManager;
import matchingengine.dto.TradeResult;

public class Matcher {

    private OrderBook book;
    private OrderManager manager;
    private PegManager pegManager;

    public Matcher(OrderBook book, OrderManager manager, PegManager pegManager) {
        this.book = book;
        this.manager = manager;
        this.pegManager = pegManager;
    }

    public List<TradeResult> matchLimit(Order order) {
        List<TradeResult> trades = new ArrayList<>();

        if (order.getSide() == Order.Side.BUY) {
            while (order.getQty() > 0 && !book.isEmpty(Order.Side.SELL)
                    && book.askPrice() <= order.getPrice()) {
                  trades.add(executeTradeBook(order, book.ask()));
            }
        } else {
            while (order.getQty() > 0 && !book.isEmpty(Order.Side.BUY)
                    && book.bidPrice() >= order.getPrice()) {
                trades.add(executeTradeBook(order, book.bid()));
            }
        }

       
        return trades;
    }

    public List<TradeResult> matchMarket(Order order) {
        List<TradeResult> trades = new ArrayList<>();

        if (order.getSide() == Order.Side.BUY) {
            while (order.getQty() > 0 && !book.isEmpty(Order.Side.SELL)) {
                trades.add(executeTradeBook(order, book.ask()));
            }
        } else {
            while (order.getQty() > 0 && !book.isEmpty(Order.Side.BUY)) {
               trades.add(executeTradeBook(order, book.bid()));
            }
        }

        return trades;
    }

    private TradeResult executeTradeBook(Order newO, Order bookO) {
        int qty = Math.min(newO.getQty(), bookO.getQty());
        long price = bookO.getPrice();

        newO.setQty(newO.getQty() - qty);
        bookO.setQty(bookO.getQty() - qty);


        if (newO.getQty() == 0) {
            book.remove(newO);
            manager.remove(newO.getId());
            pegManager.remove(newO);
        }

        if (bookO.getQty() == 0) {
            book.remove(bookO);
            manager.remove(bookO.getId());
            pegManager.remove(bookO);
        }

        return new TradeResult(price, qty);
    }
}

