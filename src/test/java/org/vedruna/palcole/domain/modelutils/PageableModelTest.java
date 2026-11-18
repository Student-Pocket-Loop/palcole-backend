package org.vedruna.palcole.domain.modelutils;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PageableModelTest {

  @Test
  @DisplayName("Test PageableModel constructor con todos los campos Ok")
  void allArgsConstructorOkTest() {

    // EJECUCIÓN
    PageableModel res = new PageableModel(2, 20, "name: ASC");

    // VALIDACIONES
    assertAll(
        () -> {
          assertEquals(2, res.getPage());
          assertEquals(20, res.getSize());
          assertEquals("name: ASC", res.getSort());
        });
  }

  @Test
  @DisplayName("Test PageableModel setters Ok")
  void settersOkTest() {

    // EJECUCIÓN
    PageableModel res = new PageableModel();
    res.setPage(1);
    res.setSize(5);
    res.setSort("UNSORTED");

    // VALIDACIONES
    assertAll(
        () -> {
          assertEquals(1, res.getPage());
          assertEquals(5, res.getSize());
          assertEquals("UNSORTED", res.getSort());
        });
  }
}
