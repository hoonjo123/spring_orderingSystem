package com.encore.ordering.order.controller;


import com.encore.ordering.common.CommonResponse;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.dto.MemberCreateReqDto;
import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order.dto.OrderReqDto;
import com.encore.ordering.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class OrderController {

    /**
    @PostMapping("/order/create")
    public void test(){
        Ordering ordering = Ordering.builder()
                .build();
    }
     */
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/create")

    public ResponseEntity<CommonResponse> orderCreate(@RequestBody OrderReqDto orderReqDto){
        Ordering ordering = orderService.create(orderReqDto);
        //ResponseDto 객체에 담긴 값은 header로 나가고, body에 담긴 Map은 json 형태로 나감.
        return new ResponseEntity<>(new CommonResponse(HttpStatus.CREATED, "member is successfully created", ordering.getId()), HttpStatus.CREATED);
    }




}
