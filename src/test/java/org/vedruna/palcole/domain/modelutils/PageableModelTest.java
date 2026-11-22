package org.vedruna.palcole.domain.modelutils;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PageableModelTest {

  private PageableModel crearPageableModel(int page, int size, String sort) {
    return new PageableModel(page, size, sort);
  }

  @Test
  @DisplayName("Test constructor vacío y setters/getters Ok")
  void constructorVacioYSettersGettersOkTest() {

    // INPUT
    int pageInput = 1;
    int sizeInput = 20;
    String sortInput = "name: ASC";

    // EJECUCIÓN
    PageableModel res = new PageableModel();
    res.setPage(pageInput);
    res.setSize(sizeInput);
    res.setSort(sortInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageInput, res.getPage());
          assertEquals(sizeInput, res.getSize());
          assertEquals(sortInput, res.getSort());
        });
  }

  @Test
  @DisplayName("Test constructor vacío valores por defecto Ok")
  void constructorVacioValoresPorDefectoOkTest() {

    // EJECUCIÓN
    PageableModel res = new PageableModel();

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(0, res.getPage());
          assertEquals(0, res.getSize());
          assertNull(res.getSort());
        });
  }

  @Test
  @DisplayName("Test constructor con parámetros Ok")
  void constructorConParametrosOkTest() {

    // INPUT
    int pageInput = 0;
    int sizeInput = 10;
    String sortInput = "UNSORTED";

    // EJECUCIÓN
    PageableModel res = crearPageableModel(pageInput, sizeInput, sortInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageInput, res.getPage());
          assertEquals(sizeInput, res.getSize());
          assertEquals(sortInput, res.getSort());
        });
  }

  @Test
  @DisplayName("Test equals y hashCode Ok")
  void equalsYHashCodeOkTest() {

    // INPUT
    PageableModel model1 = crearPageableModel(0, 10, "UNSORTED");
    PageableModel model2 = crearPageableModel(0, 10, "UNSORTED");

    // EJECUCIÓN Y VALIDACIONES
    assertAll(
        () -> {
          assertTrue(model1.equals(model1));
          assertEquals(model1, model2);
          assertEquals(model1.hashCode(), model2.hashCode());
        });
  }

  @Test
  @DisplayName("Test equals y hashCode Ko")
  void equalsYHashCodeKoTest() {

    // INPUT
    PageableModel modelBase = crearPageableModel(0, 10, "UNSORTED");
    PageableModel modelDistintaPage = crearPageableModel(1, 10, "UNSORTED");
    PageableModel modelDistintoSize = crearPageableModel(0, 20, "UNSORTED");
    PageableModel modelDistintoSort = crearPageableModel(0, 10, "name: DESC");

    // EJECUCIÓN Y VALIDACIONES
    assertAll(
        () -> {
          assertFalse(modelBase.equals(null));
          assertFalse(modelBase.equals("otroTipo"));
          assertNotEquals(modelBase, modelDistintaPage);
          assertNotEquals(modelBase, modelDistintoSize);
          assertNotEquals(modelBase, modelDistintoSort);
        });
  }
}
