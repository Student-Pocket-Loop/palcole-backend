package org.vedruna.palcole.infrastructure.adapters.outbound.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.vedruna.palcole.domain.model.enums.ListingType;

class ListingTypeConverterTest {

  private final ListingTypeConverter listingTypeConverter = new ListingTypeConverter();

  @Test
  @DisplayName("Test converter convertToDatabaseColumn Ok")
  void convertToDatabaseColumnOkTest() {

    // INPUT
    ListingType attributeInput = ListingType.values()[0];

    // ESPERADO
    String dbDataEsperado = attributeInput.name().toLowerCase();

    // EJECUCIÓN
    String res = listingTypeConverter.convertToDatabaseColumn(attributeInput);

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
    ListingType attributeInput = null;

    // EJECUCIÓN
    String res = listingTypeConverter.convertToDatabaseColumn(attributeInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter convertToEntityAttribute Ok")
  void convertToEntityAttributeOkTest() {

    // INPUT
    ListingType enumEsperado = ListingType.values()[0];
    String dbDataInput = enumEsperado.name().toLowerCase();

    // EJECUCIÓN
    ListingType res = listingTypeConverter.convertToEntityAttribute(dbDataInput);

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
    ListingType res = listingTypeConverter.convertToEntityAttribute(dbDataInput);

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
        () -> listingTypeConverter.convertToEntityAttribute(dbDataInput));
  }
}
