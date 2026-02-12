// transforma preço em double para long (centavos) e vice versa
package matchingengine.util;

public final class Price {

    private Price() {}

    public static long toTicks(double price) {
        return Math.round(price * 100);
    }

    public static double toDouble(long ticks) {
        return ticks / 100.00;
       }
}
