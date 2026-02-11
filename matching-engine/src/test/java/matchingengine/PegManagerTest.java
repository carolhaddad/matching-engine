package matchingengine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import matchingengine.book.*;
import matchingengine.domain.Order;
import matchingengine.manager.*;

public class PegManagerTest {

    private OrderBook book;
    private OrderManager manager;
    private PegManager pegManager;

    @BeforeEach
    void setup() {
        book = new TreeMapOrderBook();
        manager = new OrderManager();
        pegManager = new PegManager(book);
    }

    @Test
    void testPegBuyFollowsBid() {
        Order buy1 = manager.createLimit(Order.Type.LIMIT, Order.Side.BUY, 1000, 100);
        book.add(buy1);

        Order pegBuy = manager.createLimit(Order.Type.PEG, Order.Side.BUY, 1000, 50);
        pegManager.add(pegBuy);
        book.add(pegBuy);

        Order buy2 = manager.createLimit(Order.Type.LIMIT, Order.Side.BUY, 1200, 100);
        book.add(buy2);

        pegManager.updateBid(1200);

        assertEquals(1200, pegBuy.getPrice());
    }

    @Test
    void testPegSellFollowsAsk() {
    Order sell1 = manager.createLimit(Order.Type.LIMIT, Order.Side.SELL, 2000, 100);
    book.add(sell1);

    Order pegSell = manager.createLimit(Order.Type.PEG, Order.Side.SELL, 2500, 50);
    pegManager.add(pegSell);
    book.add(pegSell);

    Order sell2 = manager.createLimit(Order.Type.LIMIT, Order.Side.SELL, 1800, 100);
    book.add(sell2);

    pegManager.updateAsk(1800);

    assertEquals(1800, pegSell.getPrice());
}


}
