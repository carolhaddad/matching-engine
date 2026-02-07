package matchingengine;
import java.util.*;

public class PegManager {
    private List<Order> pegs = new ArrayList<>();

    public void add(Order o) { pegs.add(o); }

    public void remove(Order o) { pegs.remove(o); }

    public void update(double bid, double ask) {
        for(Order o : pegs) {
            if(o.getSide() == Order.Side.BUY) o.setPrice(bid);
            else o.setPrice(ask);
        }
    }
}

//separar bid de ask