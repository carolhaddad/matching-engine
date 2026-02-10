package matchingengine.dto;

import java.util.List;

public class BookSnapshot {

    public static class Level {
        public final long price;
        public final int qty;

        public Level(long price, int qty) {
            this.price = price;
            this.qty = qty;
        }
    }

    public final List<Level> buys;
    public final List<Level> sells;

    public BookSnapshot(List<Level> buys, List<Level> sells) {
        this.buys = buys;
        this.sells = sells;
    }
}
