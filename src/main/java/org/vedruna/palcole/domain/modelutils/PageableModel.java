package org.vedruna.palcole.domain.modelutils;

/**
 * Modelo que encapsula los parámetros de una petición de paginación.
 *
 * <p>{@code @Data}: Métodos de acceso automáticos. {@code @NoArgsConstructor}: Constructor
 * predeterminado. {@code @AllArgsConstructor}: Constructor con todos los campos.
 */

public class PageableModel {
  /** El número de página solicitado (empezando por 0). */
  private int page;

  /** La cantidad de elementos por página deseada. */
  private int size;

  /** La propiedad de ordenamiento. */
  private String sort;

  public PageableModel() {}

  public PageableModel(int page, int size, String sort) {
    this.page = page;
    this.size = size;
    this.sort = sort;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public String getSort() {
    return sort;
  }

  public void setSort(String sort) {
    this.sort = sort;
  }
}
