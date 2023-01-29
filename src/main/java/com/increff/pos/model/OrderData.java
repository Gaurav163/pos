package com.increff.pos.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class OrderData {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private ZonedDateTime datetime;
    private List<OrderItemData> items;
}
