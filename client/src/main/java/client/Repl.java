package client;

import ui.EscapeSequences;

import java.util.Scanner;
import static ui.EscapeSequences.*;

//import client.websocket.NotificationHandler;
//import webSocketMessages.Notification;


public class Repl {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println(WHITE_KING + "Welcome to Chess! Login in to start.");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("Quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
            if(!line.equals("H") && !line.equals("h") && !line.equals("Help")){
                System.out.println(SET_TEXT_COLOR_WHITE);
                client.displayMenu();
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + SET_TEXT_COLOR_GREEN);
    }
}
