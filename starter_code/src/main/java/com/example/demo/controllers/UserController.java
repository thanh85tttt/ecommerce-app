package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

  private static final Logger log = LogManager.getLogger(UserController.class);

  private final UserRepository userRepository;

  private final CartRepository cartRepository;

  private final BCryptPasswordEncoder passwordEncoder;

  public UserController(UserRepository userRepository,
                        CartRepository cartRepository,
                        BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.cartRepository = cartRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<User> findById(@PathVariable Long id) {
    return ResponseEntity.of(userRepository.findById(id));
  }

  @GetMapping("/{username}")
  public ResponseEntity<User> findByUserName(@PathVariable String username) {
    User user = userRepository.findByUsername(username);
    return user == null
        ? ResponseEntity.notFound().build()
        : ResponseEntity.ok(user);
  }

  @PostMapping("/create")
  public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
    log.info("Create new user!");

    User user = new User();
    user.setUsername(createUserRequest.getUsername());
    Cart cart = new Cart();
    cartRepository.save(cart);
    user.setCart(cart);

    String password = createUserRequest.getPassword();
    String confirmPassword = createUserRequest.getConfirmPassword();
    if (password.length() < 7
        || !password.equals(confirmPassword)) {
      log.error("Password or confirm password is invalid!");
      return ResponseEntity.badRequest().build();
    }
    user.setPassword(
        passwordEncoder.encode(password)
    );
    userRepository.save(user);
    log.info("Create new user successfully!");
    return ResponseEntity.ok(user);
  }

}