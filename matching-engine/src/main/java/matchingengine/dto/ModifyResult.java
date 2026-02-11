package matchingengine.dto;

public record ModifyResult(
    long orderId,
    boolean success,
    long newPrice,
    int newQty
) {}

