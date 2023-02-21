import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import static Server.ServerConfig.port;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        System.out.println(engine.search("бизнес"));

        // здесь создайте сервер, который отвечал бы на нужные запросы
        // слушать он должен порт 8989
        // отвечать на запросы /{word} -> возвращённое значение метода search(word) в JSON-формате

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен!");

            while (true) {
                // ждем подключения
                try (Socket clientSocket = serverSocket.accept()) {
                    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                            // ваш код
                            System.out.println("Обнаружено новое соединение по порту " + clientSocket.getPort());

                            if (clientSocket.getPort() > 0) {

                                out.println("Какое слово будем искать?");
                            }

                            System.out.println(in.readLine());

                            out.println("начал поиск");

                            if ("end".equals(in.readLine())) {
                                break;
                            }
                        }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch(IOException e){
                throw new RuntimeException(e);
            }


            //Поиск слов в тексте
//        BooleanSearchEngine booleanSearchEngine = new BooleanSearchEngine(text);
//        boolean r = booleanSearchEngine.hasWord("copy");
//        System.out.println();
//        System.out.println("===============================================================");
//        System.out.println("задача 2");
//        System.out.println(r);


        }}

    }
}