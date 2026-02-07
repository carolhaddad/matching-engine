package matchingengine;

public class Trade {
    private final double price;
    private final int qty;
    private final long buyId;
    private final long sellId;

    public Trade(double price, int qty, long buyId, long sellId) {
        this.price = price;
        this.qty = qty;
        this.buyId = buyId;
        this.sellId = sellId;
    }

    public double getPrice() { return price; }
    public int getQty() { return qty; }
    public long getBuyId() { return buyId; }
    public long getSellId() { return sellId; }
}
