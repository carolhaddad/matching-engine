package matchingengine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import matchingengine.book.*;
import matchingengine.domain.Order;
import matchingengine.manager.OrderManager;

public class OrderBookTest {

    private OrderBook book;
    private OrderManager manager;

    @BeforeEach
    void setup() {
        book = new TreeMapOrderBook();
        manager = new OrderManager();
    }

    @Test
    void testBestBidAndAsk() {
        book.add(manager.createLimit(Order.Type.LIMIT, Order.Side.BUY, 1000, 100));
        book.add(manager.createLimit(Order.Type.LIMIT, Order.Side.BUY, 2000, 100));
        book.add(manager.createLimit(Order.Type.LIMIT, Order.Side.SELL, 1000, 100));
        book.add(manager.createLimit(Order.Type.LIMIT, Order.Side.SELL, 2000, 100));


        assertEquals(2000, book.bidPrice());
        assertEquals(1000, book.askPrice());
    }

    @Test
    void marketSellDontBook() {
        manager.createLimit(Order.Type.MARKET, Order.Side.BUY, 1000, 100);
        
        assertEquals(-1, book.bidPrice());
    }


    @Test
    void negativeNumber() {
    manager.createLimit(Order.Type.LIMIT, Order.Side.BUY, -10, 100);
    assertEquals(-1, book.bidPrice());
    }

}