package com.imunnic.telegramBot.repositorios;

import com.imunnic.telegramBot.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path="usuarios", itemResourceRel = "usuario", collectionResourceRel = "usuarios")
public interface UsuarioDAO extends JpaRepository<Usuario, Long> {
  @RestResource(path = "nombreUsuario")
  public List<Usuario> findByAlias(String alias);
}
