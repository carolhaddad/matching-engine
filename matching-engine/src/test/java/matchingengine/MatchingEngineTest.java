package matchingengine;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import matchingengine.facade.MatchingEngine;
import matchingengine.dto.*;

public class MatchingEngineTest {

    private MatchingEngine engine;

    @BeforeEach
    void setup() {
        engine = new MatchingEngine();
    }

    //Testes de match limit e market
    @Test
    void testLimitBuyAndSellMatch() {
        // testa match de ordens limit e se trades são retornadas corretamente
        engine.submitLimitSell(2000, 100);
        SubmitResult result = engine.submitLimitBuy(2000, 50);

        List<TradeResult> trades = result.trades();

        assertEquals(1, trades.size());
        assertEquals(2000, trades.get(0).price());
        assertEquals(50, trades.get(0).qty());
        
        engine.submitLimitBuy(2000, 100);
        SubmitResult result2 = engine.submitLimitSell(2000, 50);

        List<TradeResult> trades2 = result2.trades();

        assertEquals(1, trades2.size());
        assertEquals(2000, trades2.get(0).price());
        assertEquals(50, trades2.get(0).qty());

    } 

    @Test
    void testMarketBuyMatch() {
        // testa match de ordens limit e market opostas
        engine.submitLimitSell(2000, 100);
        SubmitResult result = engine.submitMarketBuy(80);

        List<TradeResult> trades = result.trades();

        assertEquals(1, trades.size());
        assertEquals(80, trades.get(0).qty());
        assertEquals(2000, trades.get(0).price());
    }

    @Test
    void testMarketSellMatch() {
        engine.submitLimitBuy(1500, 100);
        SubmitResult result = engine.submitMarketSell(60);

        assertEquals(1, result.trades().size());
        assertEquals(60, result.trades().get(0).qty());
        assertEquals(1500, result.trades().get(0).price());
    } 

   @Test
    void testMarketSellMatchesBestBid(){
        // testa se market da match com melhor preço
        engine.submitLimitBuy(1500, 100);
        engine.submitLimitBuy(2000, 100);
        SubmitResult result = engine.submitMarketSell(60);

        assertEquals(1, result.trades().size());
        assertEquals(60, result.trades().get(0).qty());
        assertEquals(2000, result.trades().get(0).price());
    }

        @Test
    void testMarketBuyExtraQty() {
        // testa ordem market com quantidade maior do que há no livro
        engine.submitLimitSell(2000, 100);
        SubmitResult result = engine.submitMarketBuy(130);

        List<TradeResult> trades = result.trades();

        assertEquals(1, trades.size());
        assertEquals(100, trades.get(0).qty());
        assertEquals(2000, trades.get(0).price());

        BookSnapshot snap = engine.printBook();
        assertTrue(snap.buys.isEmpty());
        assertTrue(snap.sells.isEmpty());
        
    }

 //Testes de prioridade
   
    @Test
    void testTimePriority() {
        //testa se trade ocorre seguindo prioridade de tempo
        engine.submitLimitSell(2000, 100);
        engine.submitLimitSell(2000, 200);

        SubmitResult result = engine.submitMarketBuy(150);

        assertEquals(2, result.trades().size());

        assertEquals(100, result.trades().get(0).qty());
        assertEquals(50, result.trades().get(1).qty());
    }
    
    @Test
    void testPricePriority() {
        // testa se trade ocorre seguindo prioridade de preço
        engine.submitLimitSell(2000, 200);
        engine.submitLimitSell(1000, 100);

        SubmitResult result = engine.submitMarketBuy(150);

        assertEquals(2, result.trades().size());

        assertEquals(100, result.trades().get(0).qty());
        assertEquals(50, result.trades().get(1).qty());
    }

    @Test
    void testPriceTimePriority() {
        //testa se trade segue corretamente regra de prioridade preço-tempo
        engine.submitLimitSell(2000, 40);
        engine.submitLimitSell(2000, 50);
        engine.submitLimitSell(1000, 100);


        SubmitResult result = engine.submitMarketBuy(150);

        assertEquals(3, result.trades().size());

        assertEquals(100, result.trades().get(0).qty());
        assertEquals(40, result.trades().get(1).qty());
        assertEquals(10, result.trades().get(2).qty());
       
    }


// Testes do que aparece ou não no livro

    @Test
    void testLimitWithoutTradeAppearsInBook() {
        //limit não executada aparece no livro

    engine.submitLimitBuy(1000, 100);

    BookSnapshot snap = engine.printBook();

    assertEquals(1, snap.buys.size());
    assertEquals(1000, snap.buys.get(0).price);
    assertEquals(100, snap.buys.get(0).qty);
}

@Test
void testLimitParcialTradeAppearsInBook() {
    // testa se ordem parcialmente executada ainda vai pro livro e com quantidade correta
    engine.submitLimitBuy(1000, 100);
    engine.submitMarketSell(50);

    BookSnapshot snap = engine.printBook();

    assertEquals(1, snap.buys.size());
    assertEquals(1000, snap.buys.get(0).price);
    assertEquals(50, snap.buys.get(0).qty);
}

@Test
void testOrderFullyExecutedIsRemovedFromBook() {
    // testa se ordem totalmente executada sai do livro
    engine.submitLimitSell(2000, 100);
    engine.submitMarketBuy(100);

    BookSnapshot snap = engine.printBook();

    assertTrue(snap.sells.isEmpty());
} 

@Test
void testOrderCancelledIsRemovedFromBook() {
    //Testa se ordem cancelada sai do livro
    SubmitResult res = engine.submitLimitSell(2000, 100);
    engine.cancelOrder(res.orderId());

    BookSnapshot snap = engine.printBook();

    assertTrue(snap.sells.isEmpty());
} 


//Cancel and modify

 @Test
    void testCancelOrder() {
        // testa de cancelamento funciona corretamente e retorna true
        SubmitResult res = engine.submitLimitBuy(1000, 100);
        long id = res.orderId();

        CancelResult cancel = engine.cancelOrder(id);

        assertTrue(cancel.cancelled());
    }


@Test
void testCancelNonExistingOrder() {
    //Tenta cancelar ordem inexistente
    CancelResult result = engine.cancelOrder(9999L);

    assertFalse(result.cancelled());

    BookSnapshot snap = engine.printBook();
    assertTrue(snap.buys.isEmpty());
    assertTrue(snap.sells.isEmpty());

    
}

@Test
void testCancelAlreadyExecutedOrder() {
    // Testa cancelar ordem executada
    SubmitResult res = engine.submitLimitSell(2000, 100);
    engine.submitMarketBuy(100);

    CancelResult cancel = engine.cancelOrder(res.orderId());

    assertFalse(cancel.cancelled());
}


    @Test
    void testModifyOrder() {
        // testa funcionamento correto de modify/update
        SubmitResult res = engine.submitLimitBuy(1000, 100);
        long id = res.orderId();

        ModifyResult mod = engine.updateOrder(id, 1200, 50);

        assertTrue(mod.modified());
        assertEquals(1200, mod.newPrice());
        assertEquals(50, mod.newQty());
    }

@Test
void testModifyLosesPriority() {
    // testa se modify/update perde prioridade
    SubmitResult o1 = engine.submitLimitSell(2000, 50);
    SubmitResult o2 = engine.submitLimitSell(2000, 100);

    engine.updateOrder(o1.orderId(), 2000, 300);

    SubmitResult result = engine.submitMarketBuy(150);

    assertEquals(2, result.trades().size());
    assertEquals(100, result.trades().get(0).qty());
    assertEquals(50, result.trades().get(1).qty());
}

@Test
void testModifyNonExistingOrder() {
    //tenta modificar ordem inexistente
    ModifyResult result = engine.updateOrder(9999L, 1500, 10);

    assertFalse(result.modified());
}

@Test
void testModifyexecutedOrder() {
    //tenta modificar ordem já executada
    SubmitResult o = engine.submitLimitSell(2000, 100);
    engine.submitMarketBuy(100);
    ModifyResult result = engine.updateOrder(o.orderId(), 1500, 10);
    assertFalse(result.modified());
}

//BookSnapshot

    @Test
    void testBookSnapshot() {
        // testa snapshot do book
        engine.submitLimitBuy(1000, 100);
        engine.submitLimitSell(2000, 200);
        engine.submitLimitBuy(1500, 300);
        engine.submitLimitSell(2500, 400);

        BookSnapshot snap = engine.printBook();

        assertEquals(2, snap.buys.size());
        assertEquals(2, snap.sells.size());

        assertEquals(1500, snap.buys.get(0).price);
        assertEquals(2000, snap.sells.get(0).price);
        assertEquals(1000, snap.buys.get(1).price);
        assertEquals(2500, snap.sells.get(1).price);
    } 

@Test
void testSnapshotIsImmutable() {
    //Snapshot não altera com comando externo
    engine.submitLimitBuy(1000, 100);

    BookSnapshot snap = engine.printBook();
    snap.buys.clear(); 

    BookSnapshot newSnap = engine.printBook();

    assertEquals(1, newSnap.buys.size());
}

@Test
void testSnapshotOnEmptyBook() {
    //SnapShot em livro vazio
    BookSnapshot snap = engine.printBook();

    assertTrue(snap.buys.isEmpty());
    assertTrue(snap.sells.isEmpty());
}


//Peg sem referência de bid/ask
@Test
void testPegWithoutReference() {
    SubmitResult res = engine.submitPegSell(100);
    assertEquals(0, res.price());
}



}

