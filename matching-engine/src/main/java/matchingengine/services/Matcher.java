package matchingengine.services;

import java.util.*;

import matchingengine.book.OrderBook;
import matchingengine.domain.Order;
import matchingengine.domain.Trade;
import matchingengine.manager.OrderManager;
import matchingengine.manager.PegManager;

public class Matcher {

    private OrderBook book;
    private OrderManager manager;
    private PegManager pegManager;

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
                // OBS: se for pela regra do book:
                //trades.add(executeTradeBook(order, book.ask()));
            }
        } else {
            while (order.getQty() > 0 && !book.isEmpty(Order.Side.BUY)
                    && book.bidPrice() >= order.getPrice()) {
                trades.add(executeTrade(book.bid(), order)); 
                // OBS: se for pela regra do book:
                //trades.add(executeTradeBook(order, book.bid()));
            }
        }

       
        return trades;
    }

    public List<Trade> matchMarket(Order order) {
        List<Trade> trades = new ArrayList<>();

        if (order.getSide() == Order.Side.BUY) {
            while (order.getQty() > 0 && !book.isEmpty(Order.Side.SELL)) {
                trades.add(executeTrade(order, book.ask()));
                // OBS: se for pela regra do book:
                //trades.add(executeTradeBook(order, book.ask()));
            }
        } else {
            while (order.getQty() > 0 && !book.isEmpty(Order.Side.BUY)) {
                trades.add(executeTrade(book.bid(), order));
                // OBS: se for pela regra do book:
                //trades.add(executeTradeBook(order, book.bid()));
            }
        }

        return trades;
    }

    private Trade executeTrade(Order buy, Order sell) {
        int qty = Math.min(buy.getQty(), sell.getQty());
        long price = sell.getPrice();

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


   /* OBS: se for pela regra do book
    private Trade executeTradeBook(Order newO, Order bookO) {
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

        long buyId  = newO.getSide() == Order.Side.BUY ? newO.getId() : bookO.getId();
        long sellId = newO.getSide() == Order.Side.SELL ? newO.getId() : bookO.getId();


        return new Trade(price, qty, buyId, sellId);
    }
*/

}

