package matchingengine.app;

import matchingengine.facade.MatchingEngine;
import matchingengine.boundary.Price;

public class Main 
{
    public static void main( String[] args )
    {
        MatchingEngine engine = new MatchingEngine();
        
        engine.submitLimitBuy(Price.toTicks(10.25), 100);
    }
}
