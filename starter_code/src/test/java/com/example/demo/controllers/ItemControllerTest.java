package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
  @InjectMocks
  ItemController itemController;
  @Mock
  ItemRepository itemRepository;

  @Test
  @DisplayName("GetItems.Successfully")
  void getItemsSuccessfully() {
    when(itemRepository.findAll()).thenReturn(new ArrayList<>());
    assertThat(0).isEqualTo(
        Objects.requireNonNull(itemController
            .getItems()
            .getBody()
        ).size()
    );
  }

  @Test
  @DisplayName("GetItemById.Successfully")
  void getItemByIdSuccessfully() {
    when(itemRepository.findById(1L)).thenReturn(Optional.of(new Item()));
    assertThat(new Item())
        .isEqualTo(
            itemController
                .getItemById(1L)
                .getBody()
        );
  }

  @Test
  @DisplayName("GetItemsByName.IsEmptyList")
  void getItemsByNameIsEmptyList() {
    when(itemRepository.findByName(any())).thenReturn(new ArrayList<>());
    assertThat(HttpStatus.NOT_FOUND)
        .isEqualTo(
            itemController
                .getItemsByName("empty")
                .getStatusCode()
        );
  }

  @Test
  @DisplayName("GetItemsByName.IsNull")
  void getItemsByNameIsNull() {
    when(itemRepository.findByName(any())).thenReturn(null);
    assertThat(HttpStatus.NOT_FOUND)
        .isEqualTo(
            itemController
                .getItemsByName("null")
                .getStatusCode()
        );
  }

  @Test
  @DisplayName("GetItemsByName.Successfully")
  void getItemsByNameSuccessfully() {
    List<Item> items = new ArrayList<>();
    items.add(Item.builder()
        .id(1L)
        .name("test item")
        .price(BigDecimal.TEN)
        .description("test description")
        .build());
    when(itemRepository.findByName(any())).thenReturn(items);
    assertThat(HttpStatus.OK)
        .isEqualTo(
            itemController
                .getItemsByName("ok")
                .getStatusCode()
        );
  }
}
