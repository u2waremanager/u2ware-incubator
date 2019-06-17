package io.github.u2ware.spring.data.rest.keyvalue;

import org.springframework.data.annotation.Id;
//import org.springframework.data.keyvalue.annotation.KeySpace;

import lombok.Data;



//@KeySpace("user")
public @Data class User {

  @Id
  String uuid;
  String firstname;
}