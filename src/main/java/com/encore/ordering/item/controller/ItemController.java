package com.encore.ordering.item.controller;

import com.encore.ordering.common.CommonResponse;
import com.encore.ordering.item.domain.Item;
import com.encore.ordering.item.dto.ItemReqDto;
import com.encore.ordering.item.dto.ItemResDto;
import com.encore.ordering.item.dto.ItemSearchDto;
import com.encore.ordering.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


    @RestController
    public class ItemController {
        private final ItemService itemService;

        public ItemController(ItemService itemService) {
            this.itemService = itemService;
        }

        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping("/item/create")
        public ResponseEntity<CommonResponse> itemCreate(ItemReqDto itemReqDto) {
            Item item = itemService.create(itemReqDto);
            return new ResponseEntity<>(
                    new CommonResponse(HttpStatus.CREATED,
                            "item is successfully created",item.getId()),

                    HttpStatus.CREATED);
        }

        /**테스트 시 Param으로 넣어주면 될 것 같습니다.
         * category : 과일, 책, 전자기기
         * name : apple, apple2, helloapple*/
        @GetMapping("/items") //SecurityConfig 안에서 인증 불필요 정의
        public ResponseEntity<List<ItemResDto>> items(ItemSearchDto itemSearchDto, Pageable pageable){
            List<ItemResDto> itemResDtos = itemService.findAll(itemSearchDto, pageable);
            return new ResponseEntity<>(itemResDtos, HttpStatus.OK);
        }


        //postMan에서 url을 직접 확인하는 코드 'resourse'
        @GetMapping("/item/{id}/image")
        public ResponseEntity<Resource> getImage(@PathVariable Long id){
            Resource resourse = itemService.getImage(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            //body, headers, status
            return new ResponseEntity<>(resourse, headers, HttpStatus.OK);
        }

        @PreAuthorize("hasRole('ADMIN')")
        @PatchMapping("/item/{id}/update")
        public ResponseEntity<CommonResponse> itemUpdate(@PathVariable Long id, ItemReqDto itemReqDto){
            Item item = itemService.update(id, itemReqDto);
            return new ResponseEntity<>(new CommonResponse(HttpStatus.OK,"item successfully updated", item.getId()),HttpStatus.OK);
        }

        @PreAuthorize("hasRole('ADMIN')")
        @DeleteMapping("/item/{id}/delete")
        public ResponseEntity<CommonResponse> itemDelete(@PathVariable Long id){
            Item item = itemService.delete(id);
            return new ResponseEntity<>(
                    new CommonResponse(HttpStatus.OK,
                            "item is successfully deleted",item.getId()),

                    HttpStatus.OK);
        }
    }

