package matchingengine.app;

import matchingengine.boundary.Price;
import matchingengine.facade.MatchingEngine;

public class Main 
{
    public static void main( String[] args )
    {
        MatchingEngine engine = new MatchingEngine();
        engine.submitLimitBuy(Price.toTicks(100.00), 10);
        System.out.println("submetido");
    }
}
