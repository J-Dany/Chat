package Chat.gui;

import java.util.HashMap;
import Chat.server.Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class GuiController
{
    /**
     * The binding from client's name and
     * its label
     */
    private HashMap<String, Label> clients = new HashMap<>();

    /**
     * List view of users
     */
    @FXML
    private ListView<Label> listUsers;

    /**
     * List view of logs
     */
    @FXML
    private ListView<Label> logs;

    @FXML
    public void initialize() { }

    @FXML
    void handleClicked(MouseEvent event)
    {
        Platform.exit();
    }

    @FXML
    void handleClickStopServer(MouseEvent event) 
    {
        try
        {
            Server.server.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void handleClickOnInfo(MouseEvent event)
    {
        System.out.println("Salve");
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/info.fxml"));

            Scene scene = new Scene(root);

            Stage stage = new Stage();

            stage.setTitle("Info");
            stage.setScene(scene);
            stage.setResizable(false);

            stage.initOwner(ServerGUI.getInstance().getPrimaryStage());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

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
}