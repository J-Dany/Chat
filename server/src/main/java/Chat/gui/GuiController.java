package Chat.gui;

import java.util.HashMap;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class GuiController 
{
    private HashMap<String, Label> clients = new HashMap<>();

    @FXML
    private ListView<Label> listUsers;

    @FXML
    private ListView<Label> logs;

    @FXML
    public void initialize() { }

    public void upgradeLogs(Label l)
    {
        logs.getItems().add(l);
    }

    public void upgradeListUsers(String c)
    {
        Label l = new Label(c);
        l.setTextFill(Color.WHITE);

        clients.put(c, l);

        listUsers.getItems().add(l);
    }

    public void removeUser(String c)
    {
        if (clients.containsKey(c))
        {
            listUsers.getItems().remove(clients.get(c));
        }
    }

    @FXML
    void handleClicked(MouseEvent event)
    {
        Platform.exit();
    }
}