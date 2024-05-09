package com.imunnic.telegramBot;

import com.imunnic.telegramBot.repositorios.MensajeDAO;
import com.imunnic.telegramBot.repositorios.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class TelegramBotApplication {
	@Autowired
	private UsuarioDAO usuarioDAO;
	@Autowired
	private MensajeDAO mensajeDAO;

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(TelegramBotApplication.class, args);
		TelegramBotApplication app = context.getBean(TelegramBotApplication.class);
		app.initBot();
	}
	public void initBot() {
		OICBot oicBot = new OICBot(usuarioDAO, mensajeDAO);
		try {
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
			telegramBotsApi.registerBot(oicBot);
		} catch (TelegramApiException t) {
			t.printStackTrace();
		}
	}
}
