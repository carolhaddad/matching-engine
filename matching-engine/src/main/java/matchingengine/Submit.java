package matchingengine;

public class Submit {

    private OrderBook book;
    private OrderManager manager;
    private PegManager pegManager;

    public Submit(OrderBook book, OrderManager manager, PegManager pegManager) {
        this.book = book;
        this.manager = manager;
        this.pegManager = pegManager;
    }

    public Order submitLimitBuy(double price, int qty) {
        validate(price, qty);
        Order o = manager.createLimit(Order.Type.LIMIT, Order.Side.BUY, price, qty);
        book.add(o);
        return o;
    }

    public Order submitLimitSell(double price, int qty) {
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
        double price = book.isEmpty(Order.Side.BUY) ? 0 : book.bidPrice();
        Order o = manager.createLimit(Order.Type.PEG, Order.Side.BUY, price, qty);
        pegManager.add(o);
        book.add(o);
        return o;
    }
    public Order submitPegSell(int qty) {
        validate(1, qty);
        double price = book.isEmpty(Order.Side.BUY) ? 0 : book.askPrice();
        Order o = manager.createLimit(Order.Type.PEG, Order.Side.SELL, price, qty);
        pegManager.add(o);
        book.add(o);
        return o;
    }

    public void cancel(long id) {
        Order o = manager.get(id);
        if (o == null) return;
        book.remove(o);
        pegManager.remove(o);
        manager.remove(id);
    }

    public void updateOrder(long id, double newPrice, int newQty) {
        validate(newPrice, newQty);
        Order o = manager.get(id);
        if (o == null) return;
        book.remove(o);

        if (o.getType() == Order.Type.PEG) {
            o.setQty(newQty);
        } else {
            o.setPrice(newPrice);
            o.setQty(newQty);
        }   

        manager.updateTimePriority(o);
        book.add(o);
    }

    private void validate(double price, int qty) {
        if (qty <= 0)
            throw new IllegalArgumentException("Quantity must be positive");
        if (price < 0)
            throw new IllegalArgumentException("Price cannot be negative");
    }
}
