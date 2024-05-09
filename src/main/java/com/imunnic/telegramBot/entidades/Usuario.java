package com.imunnic.telegramBot.entidades;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "USUARIOS")
public class Usuario {
  @Id
  private Long id;
  private String nombre;
  private String apellido;
  private String alias;
  private String idioma;
  @OneToMany(mappedBy = "usuario")
  private List<Mensaje> mensajes;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getIdioma() {
    return idioma;
  }

  public void setIdioma(String idioma) {
    this.idioma = idioma;
  }

  public List<Mensaje> getMensaje() {
    return mensajes;
  }

  public void setMensaje(List<Mensaje> mensaje) {
    this.mensajes = mensaje;
  }

  public Usuario() {

  }

  public Usuario(String idioma, String alias, String apellido, String nombre, Long id) {
    this.idioma = idioma;
    this.alias = alias;
    this.apellido = apellido;
    this.nombre = nombre;
    this.id = id;
  }

  @Override
  public String toString() {
    return "Usuario:\n" + "Id: " + id + "\nNombre: " + nombre + "\nApellido: " + apellido
        + "\nAlias: " + alias + "\nIdioma: " + idioma + "\n----------\n";
  }
}
