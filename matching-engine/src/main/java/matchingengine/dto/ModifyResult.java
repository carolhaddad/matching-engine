package matchingengine.dto;

public record ModifyResult(
    long orderId,
    boolean success,
    double newPrice,
    int newQty
) {}

