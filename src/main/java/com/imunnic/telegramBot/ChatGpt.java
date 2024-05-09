package com.imunnic.telegramBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ChatGpt {
  private static final String url = "https://api.openai.com/v1/chat/completions";
  private static final String bearerToken = "";
  private static final String model = "gpt-3.5-turbo";

  public static String chatGPT(String prompt) {
    try {
      URL obj = new URL(url);
      HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
      connection.setRequestProperty("Content-Type", "application/json");

      String body =
          "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";
      connection.setDoOutput(true);
      OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
      writer.write(body);
      writer.flush();
      writer.close();

      BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;

      StringBuffer response = new StringBuffer();

      while ((line = br.readLine()) != null) {
        response.append(line);
      }
      br.close();

      // calls the method to extract the message.
      return extractMessageFromJSONResponse(response.toString());
    } catch (ProtocolException e) {
      throw new RuntimeException(e);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String extractMessageFromJSONResponse(String response) {
    int start = response.indexOf("content") + 11;

    int end = response.indexOf("\"", start);

    return response.substring(start, end);

  }
}
