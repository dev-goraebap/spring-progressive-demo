package xyz.goraebap.spring_progressive_demo.app.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class SeriesPostOrderRequest {
    private List<OrderItem> items;

    @Data
    public static class OrderItem {
        private Long id;
        private int sortOrder;
    }
}
