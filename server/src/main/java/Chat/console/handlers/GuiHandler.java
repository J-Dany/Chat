package Chat.console.handlers;

import java.util.ArrayList;

import Chat.console.CloseConsoleException;
import Chat.console.Handler;
import Chat.console.UnexpectedClosedConsole;
import Chat.server.Server;

/**
 * @author Daniele Castiglia
 */
public class GuiHandler implements Handler
{
    @Override
    public void handle(ArrayList<String> args) throws CloseConsoleException, UnexpectedClosedConsole 
    {
        Server.server.startGui();
    }   
}