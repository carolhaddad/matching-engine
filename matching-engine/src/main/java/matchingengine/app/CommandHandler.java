package matchingengine.app;

import matchingengine.boundary.Price;
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
            engine.submitLimitBuy(price(s[2]), qty(s[3]));
            return;
        }

        if (s.length == 4 && s[1].equals("sell")) {
            engine.submitLimitSell(price(s[2]), qty(s[3]));
            return;
        }

        if (s.length == 5 && s[1].equals("bid") && s[2].equals("buy")) {
            engine.submitLimitBid(price(s[3]), qty(s[4]));
            return;
        }

        if (s.length == 5 && s[1].equals("ask") && s[2].equals("sell")) {
            engine.submitLimitAsk(price(s[3]), qty(s[4]));
            return;
        }

        throw new InvalidCommandException("Entrada inválida para limit");
    }

    private static void market(String[] s, MatchingEngine engine) {

        if (s.length == 3 && s[1].equals("buy")) {
            engine.submitMarketBuy(qty(s[2]));
            return;
        }

        if (s.length == 3 && s[1].equals("sell")) {
            engine.submitMarketSell(qty(s[2]));
            return;
        }

        throw new InvalidCommandException("Entrada inválida para market");
    }


    private static void peg(String[] s, MatchingEngine engine) {

        if (s.length == 4 && s[1].equals("bid") && s[2].equals("buy")) {
            engine.submitPegBuy(qty(s[3]));
            return;
        }

        if (s.length == 4 && s[1].equals("ask") && s[2].equals("sell")) {
            engine.submitPegSell(qty(s[3]));
            return;
        }

        throw new InvalidCommandException("Entrada inválida para peg");
    }

    private static void cancel(String[] s, MatchingEngine engine) {

        if (s.length == 3 && s[1].equals("order")) {
            engine.cancelOrder(id(s[2]));
            return;
        }

        throw new InvalidCommandException("Entrada inválida para cancel");
    }

    private static void modify(String[] s, MatchingEngine engine) {
       if (s.length == 5 && s[1].equals("order")) {
            engine.updateOrder(id(s[2]), price(s[3]), qty(s[4]));
            return;
        }

        throw new InvalidCommandException("Entrada inválida para modify");
    }

    private static void print(String[] s, MatchingEngine engine) {
        if (s.length == 2 && s[1].equals("book")) {
            engine.printBook();
            return;
        }

        throw new InvalidCommandException("Entrada inválida para print");
    }

    private static String[] normalize(String line) {
        return line.trim().toLowerCase().split("\\s+");
    }

    private static long price(String s) {
        try {
            return Price.toTicks(Double.parseDouble(s));
        } catch (Exception e) {
            throw new InvalidCommandException("Preço inválido");
        }
    }

    private static int qty(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
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
}
