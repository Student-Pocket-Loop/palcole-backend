package org.vedruna.palcole.domain.modelutils;

import java.io.Serializable;
import java.util.List;

/**
 * Modelo genérico para representar una página de resultados con metadatos de paginación.
 *
 * @param <T> El tipo de contenido de la página.
 *     <p>{@code @Data}: Métodos boilerplate. {@code @NoArgsConstructor}: Constructor vacío.
 *     {@code @AllArgsConstructor}: Constructor completo.
 */
public class PageModel<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  /** El listado de elementos contenidos en la página actual. */
  private List<T> content;

  /** El número total de elementos existentes en todas las páginas. */
  private long totalElements;

  /** El número total de páginas generadas según el tamaño solicitado. */
  private int totalPages;

  /** El número de elementos presentes específicamente en esta página. */
  private int numberOfElements;

  /** El tamaño máximo de elementos solicitado por página. */
  private int size;

  /** El índice de la página actual (empezando por cero). */
  private int number;

  public PageModel() {}

  public PageModel(
      List<T> content,
      long totalElements,
      int totalPages,
      int numberOfElements,
      int size,
      int number) {
    this.content = content;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
    this.numberOfElements = numberOfElements;
    this.size = size;
    this.number = number;
  }

  public List<T> getContent() {
    return content;
  }

  public void setContent(List<T> content) {
    this.content = content;
  }

  public long getTotalElements() {
    return totalElements;
  }

  public void setTotalElements(long totalElements) {
    this.totalElements = totalElements;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  public int getNumberOfElements() {
    return numberOfElements;
  }

  public void setNumberOfElements(int numberOfElements) {
    this.numberOfElements = numberOfElements;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }
}
