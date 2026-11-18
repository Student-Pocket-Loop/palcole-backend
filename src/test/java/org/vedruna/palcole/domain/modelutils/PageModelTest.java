package org.vedruna.palcole.domain.modelutils;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PageModelTest {

  @Test
  @DisplayName("Test PageModel constructor con todos los campos Ok")
  void allArgsConstructorOkTest() {

    // INPUT
    List<String> contenido = List.of("a", "b");

    // EJECUCIÓN
    PageModel<String> res = new PageModel<>(contenido, 12, 2, 2, 10, 1);

    // VALIDACIONES
    assertAll(
        () -> {
          assertEquals(contenido, res.getContent());
          assertEquals(12, res.getTotalElements());
          assertEquals(2, res.getTotalPages());
          assertEquals(2, res.getNumberOfElements());
          assertEquals(10, res.getSize());
          assertEquals(1, res.getNumber());
        });
  }

  @Test
  @DisplayName("Test PageModel setters Ok")
  void settersOkTest() {

    // INPUT
    List<String> contenido = List.of("a");

    // EJECUCIÓN
    PageModel<String> res = new PageModel<>();
    res.setContent(contenido);
    res.setTotalElements(1);
    res.setTotalPages(1);
    res.setNumberOfElements(1);
    res.setSize(10);
    res.setNumber(0);

    // VALIDACIONES
    assertAll(
        () -> {
          assertEquals(contenido, res.getContent());
          assertEquals(1, res.getTotalElements());
          assertEquals(1, res.getTotalPages());
          assertEquals(1, res.getNumberOfElements());
          assertEquals(10, res.getSize());
          assertEquals(0, res.getNumber());
        });
  }
}
