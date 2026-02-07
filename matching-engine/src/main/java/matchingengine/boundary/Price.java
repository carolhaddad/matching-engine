package matchingengine.boundary;

public final class Price {

    private static final int TICK_SIZE = 100; 

    private Price() {}

    public static long toTicks(double price) {
        return Math.round(price * TICK_SIZE);
    }

    public static double toDouble(long ticks) {
        return ticks / (double) TICK_SIZE;
    }
}
