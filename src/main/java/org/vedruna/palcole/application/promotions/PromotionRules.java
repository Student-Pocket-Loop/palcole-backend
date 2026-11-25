package org.vedruna.palcole.application.promotions;

import org.vedruna.palcole.domain.model.Promotion;

/** Reglas de negocio compartidas por los casos de uso que crean o modifican promociones. */
final class PromotionRules {

  private PromotionRules() {}

  /**
   * Comprueba que las fechas y los precios de la promoción son coherentes.
   *
   * @throws IllegalArgumentException si la fecha de fin es anterior a la de inicio o si el precio
   *     con descuento supera al precio original (se traduce a un 400).
   */
  static void validate(Promotion promotion) {
    if (promotion.getEndDate().isBefore(promotion.getStartDate())) {
      throw new IllegalArgumentException(
          "La fecha de fin no puede ser anterior a la fecha de inicio");
    }
    if (promotion.getDiscountPrice().compareTo(promotion.getOriginalPrice()) > 0) {
      throw new IllegalArgumentException(
          "El precio con descuento no puede superar al precio original");
    }
  }
}
