package com.encore.ordering.item.service;

import com.encore.ordering.item.domain.Item;
import com.encore.ordering.item.dto.ItemReqDto;
import com.encore.ordering.item.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;


    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item create(ItemReqDto itemReqDto) {
        MultipartFile multipartFile = itemReqDto.getItemImage();
        String fileName = multipartFile.getOriginalFilename();//확장자를 포함한 파일이름
        Item new_item = Item.builder()
                .name(itemReqDto.getName())
                .category(itemReqDto.getCategory())
                .price(itemReqDto.getPrice())
                .stockQuantity(itemReqDto.getStockQuantity())
                .build();
        Item item = itemRepository.save(new_item) ;
        Path path = Paths.get("/Users/hoon/Desktop/tmp",item.getId()+"_"+fileName);
        item.setImagePath(path.toString());
        try {
            byte[] bytes = multipartFile.getBytes();

            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        } catch (IOException e) {

            throw new IllegalArgumentException("image not available"); //runtime 에러 예외처리필수 (db에 들어가면 안됨)

        }

        return item;
    }

    public Item delete(Long id) {
//        1.객체를 찾아오기
        Item item = itemRepository.findById(id).orElseThrow(()->new EntityNotFoundException("not found item"));
        item.deleteItem();
        return item;
    }
}
