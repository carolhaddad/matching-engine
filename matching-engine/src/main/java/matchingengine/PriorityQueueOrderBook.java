package matchingengine;

import java.util.PriorityQueue;

public class PriorityQueueOrderBook implements OrderBook {
    private PriorityQueue<Order> buys;
    private PriorityQueue<Order> sells;

    public PriorityQueueOrderBook() {
        buys = new PriorityQueue<>();
        sells = new PriorityQueue<>();
    }

    public void add(Order o){
        if(o.getSide() == Order.Side.BUY) buys.add(o);
        else sells.add(o);
    }
    public void remove(Order o){
        if(o.getSide() == Order.Side.BUY) buys.remove(o);
        else sells.remove(o);
    }
    public Order bid(){
        return buys.peek();
    }
    public Order ask(){
        return sells.peek();
    }
    public void printBook() {
        //FAZER
    }
}
