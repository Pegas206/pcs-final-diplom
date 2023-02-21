package Server;

import Server.ServerConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientSocket {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try (Socket serverSocket = new Socket(ServerConfig.HOST, ServerConfig.port)) {
                PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                {
                    System.out.println(in.readLine());
                    out.println(scanner.nextLine());
                    System.out.println(in.readLine());
                    System.out.println("Нажмите Ввод чтобы продолжить или end чтобы выйти");
                    if ("end".equals(scanner.nextLine())) {

                        break;
                    }else {






                    }
                }


            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
