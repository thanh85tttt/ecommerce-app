package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
  @InjectMocks
  UserController userController;
  @Mock
  UserRepository userRepository;
  @Mock
  CartRepository cartRepository;
  @Mock
  BCryptPasswordEncoder passwordEncoder;

  @Test
  @DisplayName("FindById.Successfully")
  void findByIdSuccessfully() {
    when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
    assertThat(HttpStatus.OK).isEqualTo(
        userController
            .findById(1L)
            .getStatusCode()
    );
  }

  @Test
  @DisplayName("FindByUserName.NotFound")
  void findByUserNameNotFound() {
    when(userRepository.findByUsername(any())).thenReturn(null);
    assertThat(HttpStatus.NOT_FOUND).isEqualTo(
        userController
            .findByUserName("notFound")
            .getStatusCode()
    );
  }

  @Test
  @DisplayName("FindByUserName.Successfully")
  void findByUserNameSuccessfully() {
    when(userRepository.findByUsername(any())).thenReturn(new User());
    assertThat(HttpStatus.OK).isEqualTo(
        userController
            .findByUserName("success")
            .getStatusCode()
    );
  }

  @Test
  @DisplayName("CreateNewUser.PasswordIsInvalid")
  void createNewUserPasswordIsInvalid() {
    when(cartRepository.save(any())).thenReturn(new Cart());
    CreateUserRequest request = createNewUserRequest();
    request.setPassword("ThanhBT11");
    assertThat(HttpStatus.BAD_REQUEST).isEqualTo(
        userController
            .createUser(request)
            .getStatusCode()
    );
  }

  @Test
  @DisplayName("CreateNewUser.ConfirmPasswordInvalid")
  void createNewUserConfirmPasswordInvalid() {
    when(cartRepository.save(any())).thenReturn(new Cart());
    CreateUserRequest request = createNewUserRequest();
    request.setPassword("testPassword");
    request.setConfirmPassword("testPassssss");
    assertThat(HttpStatus.BAD_REQUEST).isEqualTo(
        userController
            .createUser(request)
            .getStatusCode()
    );
  }

  @Test
  @DisplayName("CreateNewUser.Successfully")
  void createNewUserSuccessfully() {
    when(cartRepository.save(any())).thenReturn(new Cart());
    CreateUserRequest request = createNewUserRequest();
    request.setPassword("testPassword");
    request.setConfirmPassword("testPassword");
    assertThat(HttpStatus.OK).isEqualTo(
        userController
            .createUser(request)
            .getStatusCode()
    );
  }

  private CreateUserRequest createNewUserRequest() {
    CreateUserRequest request = new CreateUserRequest();
    request.setUsername("ThanhBT11");
    return request;
  }
}
