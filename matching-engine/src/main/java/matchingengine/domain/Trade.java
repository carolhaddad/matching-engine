package matchingengine.domain;

public class Trade {
    private final long price;
    private final int qty;
    private final long buyId;
    private final long sellId;

    public Trade(long price, int qty, long buyId, long sellId) {
        this.price = price;
        this.qty = qty;
        this.buyId = buyId;
        this.sellId = sellId;
    }

    public long getPrice() { return price; }
    public int getQty() { return qty; }
    public long getBuyId() { return buyId; }
    public long getSellId() { return sellId; }
}
