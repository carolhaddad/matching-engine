package matchingengine;

interface OrderBook {
    void add(Order o);
    void remove(Order o);
    Order bid();
    Order ask();
    void printBook();
    double bidPrice();
    double askPrice();
    boolean isEmpty(Order.Side side);
}