package org.vedruna.palcole.domain.model;

public class Categories {
  private int id_categoria;
  private String name;

  public Categories() {
    // inicializacion
  }

  public int getId_categoria() {
    return id_categoria;
  }

  public void setId_categoria(int id_categoria) {
    this.id_categoria = id_categoria;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
