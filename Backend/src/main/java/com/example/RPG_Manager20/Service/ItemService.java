package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.ItemDTO;
import com.example.RPG_Manager20.Model.Entities.Item;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Enums.ErrorMessageUtils;
import com.example.RPG_Manager20.Repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }
    public Item save(Item item){
        return itemRepository.save(item);
    }
    public List<Item> list(){
        return itemRepository.findAll();
    }
    public void delete(Long id){
        itemRepository.deleteById(id);
    }

    public Item update(ItemDTO itemDTO, Long idItem){
        Item itemAntigo = findById(idItem);
        return itemRepository.save(itemAntigo);
    }

    public Item findById(Long idItem) {
        Item item = itemRepository.getById(idItem);
        if (item == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND, ErrorMessageUtils.ERROR_NOT_FOUND.getMessage("Item"));
        }
        return item;
    }
}
