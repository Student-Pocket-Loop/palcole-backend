package org.vedruna.palcole.infrastructure.adapters.outbound.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Locale;
import org.vedruna.palcole.domain.model.enums.ListingType;

/**
 * Convierte el enum de dominio ({@code NUEVO}) al valor en minúsculas que guarda la columna ENUM de
 * MySQL ({@code nuevo}) y viceversa.
 */
@Converter
public class ListingTypeConverter implements AttributeConverter<ListingType, String> {

  @Override
  public String convertToDatabaseColumn(ListingType attribute) {
    return attribute == null ? null : attribute.name().toLowerCase(Locale.ROOT);
  }

  @Override
  public ListingType convertToEntityAttribute(String dbData) {
    return dbData == null ? null : ListingType.valueOf(dbData.toUpperCase(Locale.ROOT));
  }
}
