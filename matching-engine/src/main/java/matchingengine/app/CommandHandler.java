package matchingengine.app;

import matchingengine.boundary.Price;
import matchingengine.dto.*;
import matchingengine.facade.MatchingEngine;

public class CommandHandler {

    public static void execute(String line, MatchingEngine engine) {

        String[] s = normalize(line);

        if (s.length == 0)
            throw new InvalidCommandException("Entrada vazia");

        switch (s[0]) {

           case "limit":
                limit(s, engine);
                return;

            case "market":
                market(s, engine);
                return;

            case "peg":
                peg(s, engine);
                return;

            case "cancel":
                cancel(s, engine);
                return;

            case "modify":
                modify(s, engine);
                return;

             case "print":
                print(s, engine);
                return;

            case "exit":
                System.exit(0);

            default:
                throw new InvalidCommandException("Entrada inválida");
        }
    }

    private static void limit(String[] s, MatchingEngine engine) {

        if (s.length == 4 && s[1].equals("buy")) {
            SubmitResult result = engine.submitLimitBuy(price(s[2]), qty(s[3]));
             if (result.qty() > 0) System.out.println("Order created: buy " + result.qty() + " @ " + Price.toDouble(result.price()) + " id " + result.orderId());

            for (TradeResult t : result.trades()) {
                System.out.println("Trade, price: " + Price.toDouble(t.price()) + ", qty: " + t.qty());
            }
            
            return;
        }

        if (s.length == 4 && s[1].equals("sell")) {
            SubmitResult result = engine.submitLimitSell(price(s[2]), qty(s[3]));
            if (result.qty() > 0) System.out.println("Order created: sell " + result.qty()+ " @ " + Price.toDouble(result.price()) + " id " + result.orderId());

            for (TradeResult t : result.trades()) {
                System.out.println("Trade, price: " + Price.toDouble(t.price()) + ", qty: " + t.qty());
            }
            return;
        }

        if (s.length == 5 && s[1].equals("bid") && s[2].equals("buy")) {
            SubmitResult result = engine.submitLimitBid(price(s[3]), qty(s[4]));
             if (result.qty() > 0) System.out.println("Order created: limit bid " + result.qty() + " @ " + Price.toDouble(result.price()) + " id " + result.orderId());

            for (TradeResult t : result.trades()) {
                System.out.println("Trade, price: " + Price.toDouble(t.price()) + ", qty: " + t.qty());
            }
            return;
        }

        if (s.length == 5 && s[1].equals("ask") && s[2].equals("sell")) {
            SubmitResult result = engine.submitLimitAsk(price(s[3]), qty(s[4]));
             if (result.qty() > 0) System.out.println("Order created: limit ask " + result.qty() + " @ " + Price.toDouble(result.price()) + " id " + result.orderId());

            for (TradeResult t : result.trades()) {
                System.out.println("Trade, price: " + Price.toDouble(t.price()) + ", qty: " + t.qty());
            }
            return;
        }

        throw new InvalidCommandException("Entrada inválida para limit");
    }

    private static void market(String[] s, MatchingEngine engine) {

        if (s.length == 3 && s[1].equals("buy")) {
            SubmitResult result = engine.submitMarketBuy(qty(s[2]));

            for (TradeResult t : result.trades()) {
                System.out.println("Trade, price: " + Price.toDouble(t.price()) + ", qty: " + t.qty());
            }
            return;
        }

        if (s.length == 3 && s[1].equals("sell")) {
            SubmitResult result = engine.submitMarketSell(qty(s[2]));

            for (TradeResult t : result.trades()) {
                System.out.println("Trade, price: " + Price.toDouble(t.price()) + ", qty: " + t.qty());
            }
            return;
        }

        throw new InvalidCommandException("Entrada inválida para market");
    }


    private static void peg(String[] s, MatchingEngine engine) {

        if (s.length == 4 && s[1].equals("bid") && s[2].equals("buy")) {
            SubmitResult result = engine.submitPegBuy(qty(s[3]));
             if (result.qty() > 0) System.out.println("Order created: peg buy " + result.qty() + " id " + result.orderId());

            for (TradeResult t : result.trades()) {
                System.out.println("Trade, price: " + Price.toDouble(t.price()) + ", qty: " + t.qty());
            }
            return;
        }

        if (s.length == 4 && s[1].equals("ask") && s[2].equals("sell")) {
            SubmitResult result = engine.submitPegSell(qty(s[3]));
             if (result.qty() > 0) System.out.println("Order created: peg sell "  + result.qty() + " id " + result.orderId());

            for (TradeResult t : result.trades()) {
                System.out.println("Trade, price: " + Price.toDouble(t.price()) + ", qty: " + t.qty());
            }
            return;
        }

        throw new InvalidCommandException("Entrada inválida para peg");
    }

    private static void cancel(String[] s, MatchingEngine engine) {

        if (s.length == 3 && s[1].equals("order")) {
            CancelResult result = engine.cancelOrder(id(s[2]));
            if (result.cancelled() == true){
                System.out.println("Order Cancelled");
                return;
            } 
            throw new InvalidCommandException("Id inexistente");
        }

        throw new InvalidCommandException("Entrada inválida para cancel");
    }

    private static void modify(String[] s, MatchingEngine engine) {
        if (s.length == 5 && s[1].equals("order")) {
            ModifyResult result = engine.updateOrder(id(s[2]), price(s[3]), qty(s[4]));

            if (result.success()) {
                System.out.println("Order id " + result.orderId() + " Modified");
                return;
            }

            throw new InvalidCommandException("Id inexistente");
        }

        throw new InvalidCommandException("Entrada inválida para modify");
    }


    private static void print(String[] s, MatchingEngine engine) {
        if (s.length == 2 && s[1].equals("book")) {
            BookSnapshot snap = engine.printBook();
            printSnapshot(snap);
            return;
        }

        throw new InvalidCommandException("Entrada inválida para print");
    }

    private static String[] normalize(String line) {
        return line.trim().toLowerCase().split("\\s+");
    }

    private static long price(String s) {
    try {
        long p = Price.toTicks(Double.parseDouble(s));
        if (p > 0) return p;
        throw new InvalidCommandException("Preço inválido");
    } catch (Exception e) {
        throw new InvalidCommandException("Preço inválido");
    }
}


    private static int qty(String s) {
    try {
        int i = Integer.parseInt(s);
        if (i > 0) return i;
        throw new InvalidCommandException("Quantidade inválida");
    } catch (NumberFormatException e) {
        throw new InvalidCommandException("Quantidade inválida");
    }
}


    private static long id(String s) {
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            throw new InvalidCommandException("ID inválido");
        }
    }

    private static void printSnapshot(BookSnapshot snap) {
    System.out.printf("%-25s | %-25s%n", "Ordens de Compra", "Ordens de Venda");
    System.out.println("--------------------------+---------------------------");

    int i = 0;
    while (i < snap.buys.size() || i < snap.sells.size()) {
        String buy = "";
        String sell = "";

        if (i < snap.buys.size()) {
            var b = snap.buys.get(i);
            buy = String.format("%d @ %.2f", b.qty, Price.toDouble(b.price));
        }

        if (i < snap.sells.size()) {
            var s = snap.sells.get(i);
            sell = String.format("%d @ %.2f", s.qty, Price.toDouble(s.price));
        }

        System.out.printf("%-25s | %-25s%n", buy, sell);
        i++;
    }
}

}
