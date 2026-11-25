package org.vedruna.palcole;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PalcoleApplication {

  public static void main(String[] args) {
    SpringApplication.run(PalcoleApplication.class, args);
  }
}
