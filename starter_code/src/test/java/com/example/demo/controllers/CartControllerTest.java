package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {
  @InjectMocks
  CartController cartController;
  @Mock
  UserRepository userRepository;
  @Mock
  ItemRepository itemRepository;
  @Mock
  CartRepository cartRepository;

  @Test
  @DisplayName("addItemToCart.UserNotFound")
  void addItemToCartUserNotFound() {
    when(userRepository.findByUsername(any())).thenReturn(null);
    assertThat(HttpStatus.NOT_FOUND)
        .isEqualTo(
            cartController
                .addTocart(new ModifyCartRequest())
                .getStatusCode()
        );
  }

  @Test
  @DisplayName("addItemToCart.ItemNotFound")
  void addItemToCartItemNotFound() {
    when(userRepository.findByUsername(any())).thenReturn(new User());
    when(itemRepository.findById(any())).thenReturn(Optional.empty());
    assertThat(HttpStatus.NOT_FOUND)
        .isEqualTo(cartController.addTocart(new ModifyCartRequest()).getStatusCode());
  }

  @Test
  @DisplayName("addItemToCart.Successfully")
  void addToCartSuccessfully() {
    User user = newUser();
    Cart cart = newCart();
    Item item = newItem();
    List<Item> items = new ArrayList<>();
    items.add(item);
    cart.setUser(user);
    cart.setItems(items);
    user.setCart(cart);

    when(userRepository.findByUsername(any())).thenReturn(user);
    when(itemRepository.findById(any())).thenReturn(Optional.of(item));

    ResponseEntity<Cart> responseEntity
        = cartController.addTocart(ModifyCartRequest.builder()
        .quantity(3)
        .build());

    assertThat(HttpStatus.OK).isEqualTo(responseEntity.getStatusCode());
    assertThat(cart).isEqualTo(responseEntity.getBody());
  }

  @Test
  @DisplayName("removeFromCart.UserNotFound")
  void removeFromCartUserNotFound() {
    when(userRepository.findByUsername(any())).thenReturn(null);
    assertThat(HttpStatus.NOT_FOUND).isEqualTo(
        cartController
            .removeFromcart(new ModifyCartRequest())
            .getStatusCode()
    );
  }

  @Test
  @DisplayName("removeFromCart.ItemNotFound")
  void removeFromCartItemNotFound() {
    when(userRepository.findByUsername(any())).thenReturn(new User());
    when(itemRepository.findById(any())).thenReturn(Optional.empty());
    assertThat(HttpStatus.NOT_FOUND)
        .isEqualTo(cartController
            .removeFromcart(new ModifyCartRequest())
            .getStatusCode()
        );
  }

  @Test
  @DisplayName("removeFromCart.Successfully")
  void removeFromCartSuccessfully() {
    User user = newUser();
    Cart cart = newCart();
    Item item = newItem();
    List<Item> items = new ArrayList<>();
    items.add(item);
    cart.setUser(user);
    cart.setItems(items);
    user.setCart(cart);

    when(userRepository.findByUsername(any())).thenReturn(user);
    when(itemRepository.findById(any())).thenReturn(Optional.of(item));

    ResponseEntity<Cart> responseEntity = cartController.removeFromcart
        (ModifyCartRequest
            .builder()
            .quantity(3)
            .build()
        );

    assertThat(HttpStatus.OK).isEqualTo(responseEntity.getStatusCode());
    assertThat(0).isEqualTo(
        Objects.requireNonNull(responseEntity.getBody())
            .getItems()
            .size()
    );
  }

  private User newUser() {
    return User.builder()
        .id(1L)
        .username("testUser")
        .password("testPassword")
        .build();
  }

  private Cart newCart() {
    return Cart.builder()
        .id(1L)
        .total(BigDecimal.TEN)
        .build();
  }

  private Item newItem() {
    return Item.builder()
        .id(1L)
        .name("test item")
        .price(BigDecimal.ONE)
        .description("test description")
        .build();
  }
}
