package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
  @InjectMocks
  OrderController orderController;
  @Mock
  UserRepository userRepository;
  @Mock
  OrderRepository orderRepository;

  @Test
  @DisplayName("Submit.UserNotFound")
  void submitUserNotFound() {
    when(userRepository.findByUsername(any())).thenReturn(null);
    assertThat(HttpStatus.NOT_FOUND)
        .isEqualTo(
            orderController.submit("notFound").getStatusCode()
        );
  }

  @Test
  @DisplayName("Submit.Successfully")
  void submitSuccessfully() {
    User user = newUser();
    Cart cart = newCart();
    user.setCart(cart);

    when(userRepository.findByUsername(any())).thenReturn(user);
    assertThat(HttpStatus.OK).isEqualTo(
        orderController
            .submit("success")
            .getStatusCode()
    );
  }

  @Test
  @DisplayName("getOrdersForUser.UserNotFound")
  void getOrdersForUserUserNotFound() {
    when(userRepository.findByUsername(any())).thenReturn(null);
    assertThat(HttpStatus.NOT_FOUND).isEqualTo(
        orderController
            .getOrdersForUser("notFound")
            .getStatusCode()
    );
  }

  @Test
  @DisplayName("getOrdersForUser.Successfully")
  void getOrdersForUserSuccessfully() {
    when(userRepository.findByUsername(any())).thenReturn(new User());
    when(orderRepository.findByUser(any())).thenReturn(new ArrayList<>());
    assertThat(HttpStatus.OK).isEqualTo(
        orderController
            .getOrdersForUser("success")
            .getStatusCode()
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
    Item item = Item.builder()
        .id(1L)
        .name("test item")
        .price(BigDecimal.TEN)
        .description("test description")
        .build();

    List<Item> items = new ArrayList<>();
    items.add(item);

    return Cart.builder()
        .id(1L)
        .total(BigDecimal.TEN)
        .items(items)
        .build();
  }
}
