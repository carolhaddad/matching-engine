package matchingengine.app;

import matchingengine.boundary.Price;
import matchingengine.facade.MatchingEngine;

import java.util.Scanner;

public class Main 
{
    public static void main( String[] args )
    {
        MatchingEngine engine = new MatchingEngine();
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print(">>> ");
            while(sc.hasNextLine()) {
                String[] s = sc.nextLine().split(" ");
                switch(s[0]) {
                    case "limit":
                            if(s[1].equals("buy")) engine.submitLimitBuy(Price.toTicks(Double.parseDouble(s[2])), Integer.parseInt(s[3]));
                            else if (s[1].equals("sell")) engine.submitLimitSell(Price.toTicks(Double.parseDouble(s[2])), Integer.parseInt(s[3]));
                            else System.out.println("Entrada inválida - aceitamos apenas sell e buy");
                        break;

                    case "market":
                        if(s[1].equals("buy")) engine.submitMarketBuy(Integer.parseInt(s[3]));
                        else if (s[1].equals("sell")) engine.submitMarketSell(Integer.parseInt(s[3]));
                        else System.out.println("Entrada inválida - aceitamos apenas sell e buy");
                        break;

                    case "cancel":
                        engine.cancelOrder(Long.parseLong(s[2]));
                        break;

                    case "modify":
                        engine.updateOrder(Long.parseLong(s[2]), Price.toTicks(Double.parseDouble(s[3])), Integer.parseInt(s[4]));
                        break;

                    case "peg":
                        if(s[1].equals("buy")) engine.submitMarketBuy(Integer.parseInt(s[3]));
                        else if (s[1].equals("sell")) engine.submitMarketSell(Integer.parseInt(s[3]));
                        else System.out.println("Entrada inválida - aceitamos apenas sell e buy");
                        break;

                    case "print":
                        engine.printBook();
                        break;

                    case "exit":
                        break;
                        
                    default:
                        sc.close();
                        return;
                }
                System.out.print(">>> ");
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
