package org.vedruna.palcole.infrastructure.adapters.outbound.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Locale;
import org.vedruna.palcole.domain.model.enums.SaleStatus;

/**
 * Convierte el enum de dominio ({@code NUEVO}) al valor en minúsculas que guarda la columna ENUM de
 * MySQL ({@code nuevo}) y viceversa.
 */
@Converter
public class SaleStatusConverter implements AttributeConverter<SaleStatus, String> {

  @Override
  public String convertToDatabaseColumn(SaleStatus attribute) {
    return attribute == null ? null : attribute.name().toLowerCase(Locale.ROOT);
  }

  @Override
  public SaleStatus convertToEntityAttribute(String dbData) {
    return dbData == null ? null : SaleStatus.valueOf(dbData.toUpperCase(Locale.ROOT));
  }
}
