package Chat.gui;

import Chat.LogMessage;
import Chat.server.Client;
import Chat.server.Server;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Daniele Castiglia
 * @version 1.0.1
 */
public class ServerGUI extends Application
{
    private boolean on = false;
    private GuiController controller;
    private static ServerGUI instance;

    public static void main(String[] args)
    {
        instance = new ServerGUI();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        ////////////////////////////////////
        // Loading the fxml file          //
        ////////////////////////////////////
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui.fxml"));
        Parent root = loader.load();

        ////////////////////////////////////
        // Giving the controller to the   //
        // instance of ServerGUI. When    //
        // javafx starts an application   //
        // it starts a new thread, not a  //
        // ServerGUI instance             //
        ////////////////////////////////////
        instance.controller = (GuiController) loader.getController();

        ////////////////////////////////////
        // When the application is ready, //
        // load into the users list view  //
        // connected clients              //
        ////////////////////////////////////
        Platform.runLater(() ->
        {
            for (Client c : Server.server.getConnectedClients())
            {
                instance.controller.upgradeListUsers(c.toString());
            }
        });

        ////////////////////////////////////
        // Instantiated a Scene object    //
        // with the given fxml            //
        ////////////////////////////////////
        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.getIcons().add(new Image(getClass().getResource("/icona_chat.png").toString()));
        primaryStage.setTitle("Server");
        primaryStage.setResizable(false);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Upgrades logs in the GUI
     * @param type type of the log
     * @param msg the message
     */
    public void upgradeLogs(LogMessage msg)
    {
        Platform.runLater(() -> 
        {
            Label l = new Label(msg.toString());

            switch(msg.getTypeOfMessage())
            {
                case ER:
                    l.setTextFill(Color.web("#f44336"));
                    l.setStyle("-fx-font-weight: bold;");
                break;
                case IN:
                    l.setTextFill(Color.web("#2196f3"));
                break;
                case OK:
                    l.setTextFill(Color.web("#4caf50"));
            }

            instance.controller.upgradeLogs(l);
        });
    }

    /**
     * Upgrade the list of users
     * 
     * @param c the client
     */
    public void upgradeListUsers(Client c)
    {
        Platform.runLater(() -> {
            instance.controller.upgradeListUsers(c.toString());
        });
    }

    /**
     * Remove the user from the list view
     * @param c the client to delete
     */
    public void removeUser(Client c)
    {
        Platform.runLater(() -> {
            instance.controller.removeUser(c.toString());
        });
    }

    /**
     * Returns either is on or not
     * @return boolean
     */
    public boolean isOn()
    {
        return this.on;
    }

    /**
     * Sets the value of "on"
     * @param value either true or false
     */
    public void setOn(boolean value)
    {
        this.on = value;
    }

    @Override
    public void stop()
    {
        on = false;
    }
}