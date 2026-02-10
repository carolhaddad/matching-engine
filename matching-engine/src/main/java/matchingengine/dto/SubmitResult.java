package matchingengine.dto;

import java.util.List;

public record SubmitResult(long orderId, long price, long qnty, List<TradeResult> trades) {}

