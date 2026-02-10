package matchingengine.app;

import matchingengine.facade.MatchingEngine;
import java.util.Scanner;

public class Main 
{
    public static void main( String[] args )
    {
        MatchingEngine engine = new MatchingEngine();
        Scanner sc = new Scanner(System.in);

        System.out.print(">>> ");

        while (sc.hasNextLine()) {
            try {
                CommandHandler.execute(sc.nextLine(), engine);
            } catch (InvalidCommandException e) {
                System.out.println("Erro: " + e.getMessage());
            }
            System.out.print(">>> ");
        }

        sc.close();
    }
}