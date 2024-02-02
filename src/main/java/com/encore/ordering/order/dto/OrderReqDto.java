package com.encore.ordering.order.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class OrderReqDto {
    private List<OrderItemDto> orderReqItemDtos;
    @Data
    private static class OrderItemDto{
        private Long itemId;
        private int count;
    }
}

/* "orderReqItemDtos" : [
{"itemId" : 1, "count" : 10}
{"itemId" : 2, "count" : 20}
}

 */



//    private List<Long> itemIds;
//    private List<Long> counts;
//}
/**==예시데이터==**
 * 지향하는 형식자체에는 맞지 않음.
 * {
 *   "itemIds" : [1,2], "counts" : [10,20]
 * }
 *  */
