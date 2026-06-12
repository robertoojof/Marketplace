package com.mps.users.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor()
@NoArgsConstructor()
@Getter
@Setter
public class User {
  private String cpf;
  private String name;
  private String email;
  private String password;
}
