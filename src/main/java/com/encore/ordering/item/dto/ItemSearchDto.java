package com.encore.ordering.item.dto;

import lombok.Data;

@Data
public class ItemSearchDto {
    private String name;
    private String category;

    /*검색시 and조건 3가지 , 2가지, 1가지
    * 1가지 : delYn = 'N'
    * 2가지 : delYn = 1)'N'and name like '%hello%' 2)delYn = 'N' and category like '%hello%'
    * 3가지 : delYn = 'N'and name like '%hello%' and category like '%hello%'
    * */



}
