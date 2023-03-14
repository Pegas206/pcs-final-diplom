import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static Server.ServerConfig.port;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        // отвечать на запросы /{word} -> возвращённое значение метода search(word) в JSON-формате
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен!");
            while (true) {
                // ждем подключения
                try (Socket clientSocket = serverSocket.accept()) {
                    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                            System.out.println("Обнаружено новое соединение по порту " + clientSocket.getPort());
                            if (clientSocket.getPort() > 0) {
                                out.println("Какое слово будем искать?");
                            }
                            ObjectMapper mapper = new ObjectMapper();
                            String json;
                            try {
                                json = mapper.writeValueAsString(engine.search(in.readLine().toLowerCase()));

                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                            out.println(json);
                            if ("end".equals(in.readLine())) {
                                break;
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Не могу стартовать сервер");
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}