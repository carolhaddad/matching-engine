package matchingengine;

import java.util.*;

public class MatchingEngine {

    private OrderBook book = new PriorityQueueOrderBook();
    private OrderManager manager = new OrderManager();
    private PegManager pegManager = new PegManager(book);

    private Submit submit = new Submit(book, manager, pegManager);
    private Matcher matcher = new Matcher(book, manager, pegManager);

    private List<Trade> trades = new ArrayList<>();

    public void submitLimitBuy(double price, int qty) {
        Order o = submit.submitLimitBuy(price, qty);
        trades.addAll(matcher.matchLimit(o));
    }

    public void submitLimitSell(double price, int qty) {
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

    public void updateOrder(long id, double price, int qty){
        submit.updateOrder(id, price, qty);
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void printBook() {
        book.printBook();
    }
}
