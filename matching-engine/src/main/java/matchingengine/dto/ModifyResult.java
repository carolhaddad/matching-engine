package matchingengine.dto;

public record ModifyResult(
    long orderId,
    boolean modified,
    long newPrice,
    int newQty
) {}

