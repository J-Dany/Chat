package Chat.console.handlers;

import java.util.ArrayList;
import Chat.console.CloseConsoleException;
import Chat.console.Handler;
import Chat.console.UnexpectedClosedConsole;
import Chat.server.Client;
import Chat.server.Server;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class ConnectedHandler implements Handler 
{
    @Override
    public void handle(ArrayList<String> args) throws CloseConsoleException, UnexpectedClosedConsole 
    {
        Client[] clients = Server.server.getConnectedClients();

        if (clients.length == 1) 
        {
            System.out.println("There is only one client connected: ");
            System.out.println("> " + clients[0].getUsername() + " (" + clients[0].getAddress() + ")");
        } 
        else if (clients.length == 0) 
        {
            System.out.println("There are 0 clients connected");
        } 
        else 
        {
            System.out.println("There are " + clients.length + " clients connected: ");
            for (Client c : clients) {
                System.out.println("> " + c.getUsername() + " (" + c.getAddress() + ")");
            }
        }
    }
}