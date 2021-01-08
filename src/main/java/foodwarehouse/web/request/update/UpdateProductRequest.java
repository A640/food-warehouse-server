package foodwarehouse.web.request.update;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateProductRequest(
        @JsonProperty(value = "product_id", required = true)    int productId,
        @JsonProperty(value = "maker_id", required = true)      int makerId,
        @JsonProperty(value = "name", required = true)          String name,
        @JsonProperty(value = "category", required = true)      String category,
        @JsonProperty(value = "needs_cold", required = true)    boolean needColdStorage,
        @JsonProperty(value = "buy_price",required = true)      float buyPrice,
        @JsonProperty(value = "sell_price", required = true)    float sellPrice) {
}
