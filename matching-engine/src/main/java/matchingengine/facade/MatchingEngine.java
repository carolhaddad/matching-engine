//Fachada do sistema: orquestra Submit, Matcher, PegManager e OrderBook

package matchingengine.facade;

import java.util.*;

import matchingengine.book.OrderBook;
import matchingengine.book.TreeMapOrderBook;
import matchingengine.domain.Order;
import matchingengine.manager.PegManager;
import matchingengine.manager.OrderManager;
import matchingengine.services.Matcher;
import matchingengine.services.Submit;
import matchingengine.dto.*;

public class MatchingEngine {
    private OrderBook book;
    private OrderManager manager;
    private PegManager pegManager;
    private Submit submit;
    private Matcher matcher;

    public MatchingEngine() {
        this.book = new TreeMapOrderBook();
        this.manager = new OrderManager();
        this.pegManager = new PegManager(book);
        this.submit = new Submit(book, manager, pegManager);
        this.matcher = new Matcher(book, manager, pegManager);
    }

    public SubmitResult submitLimitBuy(long price, int qty) {
        long bid = book.bidPrice();

        Order o = submit.submitLimitBuy(price, qty);
        List<TradeResult> trades = matcher.matchLimit(o);
        
        //só atualiza pegs se o novo preço se torna melhor bid (e se for entrar no book)
        if (o.getQty() > 0 && o.getPrice() > bid) {
            pegManager.updateBid(o.getPrice());
        }
        return new SubmitResult(o.getId(), o.getPrice(), o.getQty(), trades);
    }
    public SubmitResult submitLimitSell(long price, int qty) {
        long ask = book.askPrice();
        Order o = submit.submitLimitSell(price, qty);
        List<TradeResult> trades = matcher.matchLimit(o);
        if (o.getQty() > 0 && o.getPrice() < ask) {
            pegManager.updateAsk(o.getPrice());
        }        
        return new SubmitResult(o.getId(), o.getPrice(), o.getQty(), trades);
    }

    public SubmitResult submitLimitBid(long price, int qty) {
        // se o preço informado for inferior ao melhor bid atual, ele é ajustado para o melhor bid.
        long bid = book.isEmpty(Order.Side.BUY) ? price : book.bidPrice();
        long finalPrice = Math.max(price, bid);

        Order o = submit.submitLimitBuy(finalPrice, qty);
        List<TradeResult> trades = matcher.matchLimit(o);
        if (o.getQty() > 0) {
            pegManager.updateBid(o.getPrice());
        }
    return new SubmitResult(o.getId(), o.getPrice(), o.getQty(), trades);
    }
    
    public SubmitResult submitLimitAsk(long price, int qty) {
        long ask = book.isEmpty(Order.Side.SELL) ? price : book.askPrice();
        long finalPrice = Math.min(price, ask);

        Order o = submit.submitLimitSell(finalPrice, qty);
        List<TradeResult> trades = matcher.matchLimit(o);
        if (o.getQty() > 0) {
            pegManager.updateAsk(o.getPrice());
        }
    return new SubmitResult(o.getId(), o.getPrice(), o.getQty(), trades);
    }

    public SubmitResult submitMarketBuy(int qty) {
        Order o = submit.submitMarketBuy(qty);
        List<TradeResult> trades = matcher.matchMarket(o);
        return new SubmitResult(o.getId(), o.getPrice(), o.getQty(), trades);
    }

    public SubmitResult submitMarketSell(int qty) {
        Order o = submit.submitMarketSell(qty);
        List<TradeResult> trades =matcher.matchMarket(o);
        return new SubmitResult(o.getId(), o.getPrice(), o.getQty(), trades);
    }

    public SubmitResult submitPegBuy(int qty) {
        Order o = submit.submitPegBuy(qty);
        List<TradeResult> trades = matcher.matchLimit(o);
        return new SubmitResult(o.getId(), o.getPrice(), o.getQty(), trades);
    }

    public SubmitResult submitPegSell(int qty) {
        Order o = submit.submitPegSell(qty);
        List<TradeResult> trades = matcher.matchLimit(o);
        return new SubmitResult(o.getId(), o.getPrice(), o.getQty(), trades);
    }

    public CancelResult cancelOrder(long id) {
        return submit.cancel(id);
    }

    public ModifyResult updateOrder(long id, long price, int qty){
        boolean modified = submit.updateOrder(id, price, qty);
        return new ModifyResult(id, modified, price, qty);
    }


    public BookSnapshot printBook() {
        return book.printBook();
    }
}
