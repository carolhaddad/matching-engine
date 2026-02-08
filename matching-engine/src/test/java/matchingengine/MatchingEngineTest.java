package matchingengine;

import matchingengine.boundary.Price;
import matchingengine.domain.Trade;
import matchingengine.facade.MatchingEngine;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MatchingEngineTest {

    private MatchingEngine engine;

    @BeforeEach
    void setup() {
        engine = new MatchingEngine();
    }

    @Test
    void limitMatch() {
        engine.submitLimitBuy(Price.toTicks(100.00), 10);
        engine.submitLimitSell(Price.toTicks(99.00), 5);

        List<Trade> trades = engine.getTrades();

        assertEquals(1, trades.size());

        Trade t = trades.get(0);
        assertEquals(5, t.getQty());
        assertEquals(Price.toTicks(99.00), t.getPrice());
    }

}