package matchingengine;

interface OrderBook {
    void add(Order o);
    void remove(Order o);
    Order bid();
    Order ask();
    void printBook();
    //criar um askPrice e um bidPrice
}