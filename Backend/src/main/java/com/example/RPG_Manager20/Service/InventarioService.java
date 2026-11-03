package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.Request.ItemRequestDTO;
import com.example.RPG_Manager20.Model.DTO.Response.ItemResponseDTO;
import com.example.RPG_Manager20.Model.DTO.Response.PersonagemResponseDTO;
import com.example.RPG_Manager20.Model.Entities.Item;
import com.example.RPG_Manager20.Model.Entities.Personagem;
import com.example.RPG_Manager20.Model.Mapper.ItemMapper;
import com.example.RPG_Manager20.Repository.ItemRepository;
import com.example.RPG_Manager20.Repository.PersonagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventarioService {

    @Autowired
    private PersonagemRepository personagemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PersonagemService personagemService;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemService itemService;

    // ============================================
    // CRIAR ITEM (catálogo)
    // ============================================
    @Transactional
    public ItemResponseDTO criarItem(ItemRequestDTO requestDTO) {
        Item item = itemMapper.toEntity(requestDTO);
        item = itemRepository.save(item);
        return itemMapper.toDto(item);
    }

    // ============================================
    // ADICIONAR ITEM AO INVENTÁRIO
    // ============================================
    @Transactional
    public PersonagemResponseDTO adicionarItemIventario(Long personagemId, Long itemId, int quantidade) {
        Personagem personagem = personagemService.findById(personagemId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Item com esse ID não encontrado " + itemId));

        Item itemCopia = new Item();
        itemCopia.setNomeItem(item.getNomeItem());
        itemCopia.setTipoItem(item.getTipoItem());
        itemCopia.setDescricaoItem(item.getDescricaoItem());
        itemCopia.setPrecoItem(item.getPrecoItem());
        itemCopia.setRaridadeItem(item.getRaridadeItem());
        itemCopia.setPesoItem(item.getPesoItem());
        itemCopia.setMagico(item.isMagico());
        itemCopia.setPrecisaSintonizacao(item.isPrecisaSintonizacao());
        itemCopia.setQuantidade(quantidade);
        itemCopia.setPersonagem(personagem);

        itemCopia = itemRepository.save(itemCopia);
        personagem.getInventarioPersonagem().add(itemCopia);

        Personagem salvo = personagemRepository.save(personagem);
        return personagemService.toResponseDto(salvo);
    }

    // ============================================
    // REMOVER ITEM
    // ============================================
    @Transactional
    public PersonagemResponseDTO removerItem(Long personagemId, Long itemId) {
        Personagem personagem = personagemService.findById(personagemId);

        Item item = personagem.getInventarioPersonagem().stream()
                .filter(e -> e.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Item não encontrado"));

        personagem.getInventarioPersonagem().remove(item);
        itemRepository.delete(item);

        Personagem salvo = personagemRepository.save(personagem);
        return personagemService.toResponseDto(salvo);
    }

    // ============================================
    // BUSCAR ITEM POR NOME
    // ============================================
    @Transactional
    public ItemResponseDTO buscarItemIventario(Long personagemId, String nomeItem) {
        Personagem personagem = personagemService.findById(personagemId);

        Item item = personagem.getInventarioPersonagem().stream()
                .filter(i -> i.getNomeItem().equals(nomeItem))
                .findFirst()
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Item não encontrado no inventário"));

        return itemMapper.toResponseDto(item);
    }

    // ============================================
    // ATUALIZAR QUANTIDADE
    // ============================================
    @Transactional
    public PersonagemResponseDTO atualizarQuantidadeItem(Long personagemId, Long itemId, int quantidadeNova) {
        Personagem personagem = personagemService.findById(personagemId);

        Item item = personagem.getInventarioPersonagem().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Item não encontrado no inventário"));

        if (quantidadeNova <= 0) {
            personagem.getInventarioPersonagem().remove(item);
            itemRepository.delete(item);
        } else {
            item.setQuantidade(quantidadeNova);
            itemRepository.save(item);
        }

        Personagem salvo = personagemRepository.save(personagem);
        return personagemService.toResponseDto(salvo);
    }

    // ============================================
    // LISTAR INVENTÁRIO
    // ============================================
    @Transactional
    public List<ItemResponseDTO> listarInventario(Long personagemId) {
        Personagem personagem = personagemService.findById(personagemId);
        return personagem.getInventarioPersonagem().stream()
                .map(itemMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}