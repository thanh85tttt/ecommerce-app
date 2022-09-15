package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ModifyCartRequest {
  @JsonProperty
  private String username;
  @JsonProperty
  private long itemId;
  @JsonProperty
  private int quantity;
}
