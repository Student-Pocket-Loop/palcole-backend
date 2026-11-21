package org.vedruna.palcole.infrastructure.adapters.outbound.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.vedruna.palcole.domain.model.enums.ItemCondition;

class ItemConditionConverterTest {

  private final ItemConditionConverter itemConditionConverter = new ItemConditionConverter();

  @Test
  @DisplayName("Test converter convertToDatabaseColumn Ok")
  void convertToDatabaseColumnOkTest() {

    // INPUT
    ItemCondition attributeInput = ItemCondition.NUEVO;

    // ESPERADO
    String dbDataEsperado = "nuevo";

    // EJECUCIÓN
    String res = itemConditionConverter.convertToDatabaseColumn(attributeInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(dbDataEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter convertToDatabaseColumn Null")
  void convertToDatabaseColumnNullTest() {

    // INPUT
    ItemCondition attributeInput = null;

    // EJECUCIÓN
    String res = itemConditionConverter.convertToDatabaseColumn(attributeInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter convertToEntityAttribute Ok")
  void convertToEntityAttributeOkTest() {

    // INPUT
    String dbDataInput = "nuevo";

    // ESPERADO
    ItemCondition attributeEsperado = ItemCondition.NUEVO;

    // EJECUCIÓN
    ItemCondition res = itemConditionConverter.convertToEntityAttribute(dbDataInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(attributeEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter convertToEntityAttribute Null")
  void convertToEntityAttributeNullTest() {

    // INPUT
    String dbDataInput = null;

    // EJECUCIÓN
    ItemCondition res = itemConditionConverter.convertToEntityAttribute(dbDataInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter convertToEntityAttribute Ko")
  void convertToEntityAttributeKoTest() {

    // INPUT
    String dbDataInput = "VALOR_INEXISTENTE";

    // EJECUCIÓN Y VALIDACIONES
    assertThrows(
        IllegalArgumentException.class,
        () -> itemConditionConverter.convertToEntityAttribute(dbDataInput));
  }
}
