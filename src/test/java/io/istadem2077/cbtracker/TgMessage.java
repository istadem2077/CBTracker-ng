package io.istadem2077.cbtracker;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TgMessage {
    public static String Notify(String ChatId, String Message) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(
                URI.create(
                        String.format(
                                "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&parse_mode=Markdown&text=%s",
                                "6059417392:AAHd_ravkjwRjOjTnMBMXTTnywIwxQ94U7w",
                                ChatId,
                                Message)
                )).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static void CBError(String Message) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(
                URI.create(
                        String.format(
                                "https://api.telegram.org/bot%s/sendMessage?chat_id=5670908383&parse_mode=Markdown&text=%s",
                                "6412450545:AAH0N0coNpBcQdXPzGdGTyPvBGBTs1_IwAI",
                                Message)
                )).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        response.body();
    }
}
