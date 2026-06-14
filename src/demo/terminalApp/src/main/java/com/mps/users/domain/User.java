package com.mps.users.domain;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor()
@NoArgsConstructor()
@Getter
@Setter
public class User {
  private UUID id;
  private String cpf;
  private String name;
  private String email;
  private String password;
  private Role role;
}
