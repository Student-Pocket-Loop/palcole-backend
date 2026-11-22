package org.vedruna.palcole.domain.modelutils;

import java.util.Objects;

/** Modelo que encapsula los parámetros de una petición de paginación. */
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PageableModel that = (PageableModel) o;
    return page == that.page && size == that.size && Objects.equals(sort, that.sort);
  }

  @Override
  public int hashCode() {
    return Objects.hash(page, size, sort);
  }
}
