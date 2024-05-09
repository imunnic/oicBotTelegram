package com.imunnic.telegramBot;

import com.imunnic.telegramBot.entidades.Mensaje;
import com.imunnic.telegramBot.entidades.Usuario;
import com.imunnic.telegramBot.repositorios.MensajeDAO;
import com.imunnic.telegramBot.repositorios.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Component
public class OICBot extends TelegramLongPollingBot {

  private UsuarioDAO usuarioDAO;
  
  private MensajeDAO mensajeDAO;

  private final String[] RESPUESTASINCORRECTAS =
      {"Lo siento, no entendí tu mensaje.", "Parece que no entendí lo que quisiste decir.",
          "¿Podrías intentar nuevamente? No comprendí tu mensaje.",
          "No encontré ninguna opción válida, ¿puedes intentar otra vez?",
          "Ups, parece que hubo un error. Intenta nuevamente.",
          "No estoy seguro de entender. ¿Puedes ser más claro?",
          "Lo siento, no puedo procesar ese comando.",
          "¡Oops! Algo salió mal. Intenta de nuevo más tarde.",
          "No puedo entender lo que quieres decir.", "Lo siento, esa no es una opción válida.",
          "No comprendo. ¿Puedes repetirlo de otra manera?",
          "Parece que me he confundido. Por favor, intenta otra vez.",
          "Lo siento, necesito más información para entender.",
          "¡Vaya! Esa no era la respuesta que esperaba.",
          "No estoy seguro de entender lo que quieres.",
          "Parece que ha habido un error. Por favor, inténtalo de nuevo.",
          "No puedo procesar tu solicitud en este momento.",
          "Ups, parece que no comprendí correctamente. ¿Puedes explicarlo de nuevo?",
          "Lo siento, no puedo realizar esa acción en este momento.",
          "¿Qué quisiste decir con eso? No estoy seguro de entender."};
  private final String[] CHISTES =
      {"¿Por qué los programadores odian el mar? Porque siempre están buscando bugs.",
          "¿Cuál es el animal favorito de un programador? El bufalos (buffalo = buffer overflow).",
          "¿Por qué los programadores prefieren la playa? Porque allí no hay bugs, solo errores en la arena.",
          "¿Qué le dijo un bit a otro? \"¿Me pasas tu número de byte?\"",
          "¿Cuántos programadores se necesitan para cambiar una bombilla? Ninguno, ¡es un problema de hardware!",
          "¿Por qué los programadores no les gusta el otoño? Porque siempre tienen que caer los árboles (fallar).",
          "¿Cuál es la comida favorita de los programadores? Los bits and chips.",
          "¿Por qué los programadores odian el camping? Porque detestan los bugs en el exterior.",
          "¿Cómo se llama el tío de Batman que sabe de programación? ¡Java Script!",
          "¿Cuál es el colmo de un programador? Tener una novia con demasiados 'bits' de celos."};

  @Override
  public String getBotUsername() {
    return "oicDimbot";
  }

  @Override
  public String getBotToken() {
    return "";
  }

  public OICBot(UsuarioDAO usuarioDAO, MensajeDAO mensajeDAO){
    this.usuarioDAO = usuarioDAO;
    this.mensajeDAO = mensajeDAO;
  }
  @Override
  public void onUpdateReceived(Update update) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Message mensaje = update.getMessage();
    User usuario = mensaje.getFrom();
    Mensaje mensajeDB = new Mensaje();
    Usuario usuarioNuevo = new Usuario(usuario.getLanguageCode(), usuario.getUserName(),
        usuario.getLastName(), usuario.getFirstName(), usuario.getId());
    String fecha = sdf.format(new Date(mensaje.getDate()));
    mensajeDB.setFecha(fecha);
    double latitud = (mensaje.getLocation() == null)? 0:mensaje.getLocation().getLatitude();
    double longitud = (mensaje.getLocation() == null)? 0:mensaje.getLocation().getLongitude();
    mensajeDB.setLatitud(latitud);
    mensajeDB.setLongitud(longitud);
    mensajeDB.setTexto(mensaje.getText());
    mensajeDB.setUsuario(usuarioNuevo);
    if (usuarioDAO.findByAlias(usuario.getUserName()).isEmpty()) {
      usuarioDAO.save(usuarioNuevo);
    }
    mensajeDAO.save(mensajeDB);
    if (mensaje.getText().equals("/start")) {
      String contenido =
          "Bienvenido al chat del DIM \uD83D\uDE01, " + "si quieres saber mis opciones escribe /help";
      sendText(usuario.getId(), contenido);
    } else if (mensaje.getText().equals("/help")) {
      String contenido =
          "Mis opciones son las siguientes:\n" +
              "/tirarDado\n" +
              "/tirarDardo\n" +
              "/tirarCanasta\n" +
              "/tragaperras\n" +
              "/chutar\n" +
              "/tirarBolos\n" +
              "/chiste\n" +
              "/usuarios";
      sendText(usuario.getId(), contenido);
    } else if (mensaje.getText().equals("/tirarDado")) {
      tirarDado(usuario.getId());
    } else if (mensaje.getText().equals("/tirarDardo")) {
      tirarDardo(usuario.getId());
    } else if (mensaje.getText().equals("/tirarCanasta")) {
      tirarCanasta(usuario.getId());
    } else if (mensaje.getText().equals("/tragaperras")) {
      clingCling(usuario.getId());
    } else if (mensaje.getText().equals("/chutar")) {
      chutar(usuario.getId());
    } else if (mensaje.getText().equals("/tirarBolos")) {
      tirarBolos(usuario.getId());
    } else if (mensaje.getText().equals("/chiste")) {
      Random random = new Random();
      int indice = random.nextInt(CHISTES.length);
      String respuesta = CHISTES[indice];
      sendText(usuario.getId(), respuesta);
    } else if (mensaje.getText().equals("/usuarios")) {
      infoUsuarios(usuario.getId());
    } else {
      Random random = new Random();
      int indice = random.nextInt(RESPUESTASINCORRECTAS.length);
      String respuesta = RESPUESTASINCORRECTAS[indice];
      sendText(usuario.getId(), respuesta);
    }
  }

  public void sendText(Long usuarioId, String mensaje) {
    SendMessage sm = SendMessage.builder().chatId(usuarioId.toString())
        .text(mensaje).build();
    try {
      execute(sm);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  public void tirarDado(Long usuarioId) {
    SendDice dice = new SendDice();
    dice.setChatId(usuarioId);
    dice.setEmoji("\uD83C\uDFB2");
    try {
      execute(dice);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  public void tirarCanasta(Long usuarioId) {
    SendDice dice = new SendDice();
    dice.setChatId(usuarioId);
    dice.setEmoji("\uD83C\uDFC0");
    try {
      execute(dice);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  public void tirarDardo(Long usuarioId) {
    SendDice dice = new SendDice();
    dice.setChatId(usuarioId);
    dice.setEmoji("\uD83C\uDFAF");
    try {
      execute(dice);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  public void tirarBolos(Long usuarioId) {
    SendDice dice = new SendDice();
    dice.setChatId(usuarioId);
    dice.setEmoji("\uD83C\uDFB3");
    try {
      execute(dice);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  public void chutar(Long usuarioId) {
    SendDice dice = new SendDice();
    dice.setChatId(usuarioId);
    dice.setEmoji("⚽");
    try {
      execute(dice);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  public void clingCling(Long usuarioId) {
    SendDice dice = new SendDice();
    dice.setChatId(usuarioId);
    dice.setEmoji("\uD83C\uDFB0");
    try {
      execute(dice);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  public void infoUsuarios(Long usuarioId){
    List<Usuario> usuarios = usuarioDAO.findAll();
    StringBuilder texto = new StringBuilder();
    for (Usuario usuario: usuarios) {
      texto.append(usuario.toString());
    }
    sendText(usuarioId, texto.toString());
  }
}
