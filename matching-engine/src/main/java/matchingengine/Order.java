package matchingengine;

public class Order {
    public enum Type {LIMIT, MARKET, PEG}
    public enum Side {BUY, SELL}

    private Type type;
    private Side side;
    private double price;
    private int qty;
    private long id;
    private long timePriority;

    public Order(Type type, Side side, double price, int qty, long id, long timePriority) {
        this.type = type;
        this.side = side;
        this.price = price;
        this.qty = qty;
        this.id = id;
        this.timePriority = timePriority;
    }
    
    public Type getType() { return type; }
    public Side getSide() { return side; }
    public double getPrice() { return price; }
    public int getQty() { return qty; }
    public long getId() { return id; }
    public long getTimePriority() { return timePriority; }

    public void setPrice(double price) { this.price = price; }
    public void setQty(int qty) { this.qty = qty; }
    public void setTimePriority(long timePriority) {this.timePriority = timePriority;}
}