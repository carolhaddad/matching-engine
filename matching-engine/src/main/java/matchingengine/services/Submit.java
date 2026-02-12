package matchingengine.services;

import matchingengine.book.OrderBook;
import matchingengine.domain.Order;
import matchingengine.dto.CancelResult;
import matchingengine.manager.OrderManager;
import matchingengine.manager.PegManager;

public class Submit {

    private OrderBook book;
    private OrderManager manager;
    private PegManager pegManager;

    public Submit(OrderBook book, OrderManager manager, PegManager pegManager) {
        this.book = book;
        this.manager = manager;
        this.pegManager = pegManager;
    }

    public Order submitLimitBuy(long price, int qty) {
        validate(price, qty);
        Order o = manager.createLimit(Order.Type.LIMIT, Order.Side.BUY, price, qty);
        book.add(o);
        return o;
    }

    public Order submitLimitSell(long price, int qty) {
        validate(price, qty);
        Order o =  manager.createLimit(Order.Type.LIMIT, Order.Side.SELL, price, qty);
        book.add(o);
        return o;
    }

    public Order submitMarketBuy(int qty) {
        validate(1, qty);
        return manager.createMarket(Order.Side.BUY, qty);
    }

    public Order submitMarketSell(int qty) {
        validate(1, qty);
        return manager.createMarket(Order.Side.SELL, qty);
    }
    public Order submitPegBuy(int qty) {
        validate(1, qty);
        //Se não existir bid no livro, o preço inicial da peg é 0
        long price = book.isEmpty(Order.Side.BUY) ? 0 : book.bidPrice();
        Order o = manager.createLimit(Order.Type.PEG, Order.Side.BUY, price, qty);
        pegManager.add(o);
        book.add(o);
        return o;
    }
    public Order submitPegSell(int qty) {
        validate(1, qty);
        long price = book.isEmpty(Order.Side.SELL) ? 0 : book.askPrice();
        Order o = manager.createLimit(Order.Type.PEG, Order.Side.SELL, price, qty);
        pegManager.add(o);
        book.add(o);
        return o;
    }

    public CancelResult cancel(long id) {
        Order o = manager.get(id);
        if (o == null) return new CancelResult(id, false);
        book.remove(o);
        pegManager.remove(o);
        manager.remove(id);
        return new CancelResult(o.getId(), true);
    }

    public boolean updateOrder(long id, long newPrice, int newQty) {
        validate(newPrice, newQty);
        Order o = manager.get(id);
        if (o == null) return false;
        book.remove(o);

        if (o.getType() == Order.Type.PEG) {
            o.setQty(newQty); //update peg não permite alteraçao de preço
        } else {
            o.setPrice(newPrice);
            o.setQty(newQty);
        }   

        manager.updateTimePriority(o); 
        book.add(o);
        return true;
    }

    //valida preço e quantidade antes de criar/modificar orderns
    private void validate(long price, int qty) {
        if (qty <= 0)
            throw new IllegalArgumentException("Quantity must be positive");
        if (price < 0)
            throw new IllegalArgumentException("Price cannot be negative");
    }
}
