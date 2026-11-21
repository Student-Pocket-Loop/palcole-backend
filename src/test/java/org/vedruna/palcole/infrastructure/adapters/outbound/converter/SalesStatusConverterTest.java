package org.vedruna.palcole.infrastructure.adapters.outbound.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.vedruna.palcole.domain.model.enums.SaleStatus;

class SaleStatusConverterTest {

  private final SaleStatusConverter saleStatusConverter = new SaleStatusConverter();

  @Test
  @DisplayName("Test converter convertToDatabaseColumn Ok")
  void convertToDatabaseColumnOkTest() {

    // INPUT
    SaleStatus attributeInput = SaleStatus.values()[0];

    // ESPERADO
    String dbDataEsperado = attributeInput.name().toLowerCase(Locale.ROOT);

    // EJECUCIÓN
    String res = saleStatusConverter.convertToDatabaseColumn(attributeInput);

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
    SaleStatus attributeInput = null;

    // EJECUCIÓN
    String res = saleStatusConverter.convertToDatabaseColumn(attributeInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter convertToEntityAttribute Ok")
  void convertToEntityAttributeOkTest() {

    // INPUT
    SaleStatus enumEsperado = SaleStatus.values()[0];
    String dbDataInput = enumEsperado.name().toLowerCase(Locale.ROOT);

    // EJECUCIÓN
    SaleStatus res = saleStatusConverter.convertToEntityAttribute(dbDataInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(enumEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter convertToEntityAttribute Null")
  void convertToEntityAttributeNullTest() {

    // INPUT
    String dbDataInput = null;

    // EJECUCIÓN
    SaleStatus res = saleStatusConverter.convertToEntityAttribute(dbDataInput);

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
        () -> saleStatusConverter.convertToEntityAttribute(dbDataInput));
  }
}
