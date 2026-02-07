package matchingengine.facade;

import java.util.*;

import matchingengine.book.OrderBook;
import matchingengine.book.TreeMapOrderBook;
import matchingengine.domain.Order;
import matchingengine.domain.Trade;
import matchingengine.manager.PegManager;
import matchingengine.manager.OrderManager;
import matchingengine.services.Matcher;
import matchingengine.services.Submit;

public class MatchingEngine {
    private OrderBook book;
    private OrderManager manager;
    private PegManager pegManager;
    private Submit submit;
    private Matcher matcher;
    private List<Trade> trades;

    public MatchingEngine() {
        this.book = new TreeMapOrderBook();
        this.manager = new OrderManager();
        this.pegManager = new PegManager(book);
        this.submit = new Submit(book, manager, pegManager);
        this.matcher = new Matcher(book, manager, pegManager);
        this.trades = new ArrayList<>();
    }

    public void submitLimitBuy(long price, int qty) {
        Order o = submit.submitLimitBuy(price, qty);
        trades.addAll(matcher.matchLimit(o));
    }
    public void submitLimitSell(long price, int qty) {
        Order o = submit.submitLimitSell(price, qty);
        trades.addAll(matcher.matchLimit(o));
    }

    public void submitMarketBuy(int qty) {
        Order o = submit.submitMarketBuy(qty);
        trades.addAll(matcher.matchMarket(o));
    }

    public void submitMarketSell(int qty) {
        Order o = submit.submitMarketSell(qty);
        trades.addAll(matcher.matchMarket(o));
    }

    public void submitPegBuy(int qty) {
        Order o = submit.submitPegBuy(qty);
        trades.addAll(matcher.matchLimit(o));
    }

    public void submitPegSell(int qty) {
        Order o = submit.submitPegSell(qty);
        trades.addAll(matcher.matchLimit(o));
    }

    public void cancelOrder(long id) {
        submit.cancel(id);
    }

    public void updateOrder(long id, long price, int qty){
        submit.updateOrder(id, price, qty);
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void printBook() {
        book.printBook();
    }
}
