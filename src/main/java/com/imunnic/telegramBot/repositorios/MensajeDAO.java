package com.imunnic.telegramBot.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.imunnic.telegramBot.entidades.Mensaje;
import com.imunnic.telegramBot.entidades.Usuario;

@RepositoryRestResource(path = "mensajes", itemResourceRel = "mensaje", collectionResourceRel = "mensajes")
public interface MensajeDAO extends JpaRepository<Mensaje, Long>{
  List<Mensaje> findByUsuario(Usuario usuario);
}
