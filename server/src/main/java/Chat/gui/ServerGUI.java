package Chat.gui;

import Chat.LogMessage;
import Chat.server.Client;
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
 * @version 2.0.4
 */
public class ServerGUI extends Application
{
    private static boolean on = false;
    private static GuiController controller;
    private static Stage primaryStage;

    private String title;

    /**
     * Default ctor
     */
    public ServerGUI()
    {
        this.title = "That!";
    }

    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        ServerGUI.primaryStage = primaryStage;

        primaryStage.setTitle(this.title);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui.fxml"));
        Parent root = (Parent) loader.load();

        controller = (GuiController) loader.getController();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.getIcons().add(new Image(getClass().getResource("/icona_chat.png").toString()));
        primaryStage.setScene(scene);

        on = true;
        primaryStage.show();
    }

    /**
     * Upgrades logs in the GUI
     * 
     * @param type type of the log
     * @param msg the message
     */
    public void upgradeLogs(LogMessage msg)
    {
        Platform.runLater(() -> {
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
                case WA:
                    l.setTextFill(Color.web("#ffeb3b"));
                break;
                case OK:
                    l.setTextFill(Color.web("#4caf50"));
            }

            controller.upgradeLogs(l);
        });
    }

    /**
     * Update the exitingData list view
     * 
     * @param data the data
     */
    public void updateExitingData(String data)
    {
        Platform.runLater(() -> controller.updateExitingData(data));
    }

    /**
     * Upgrade the list of users
     * 
     * @param c the client
     */
    public void upgradeListUsers(Client c)
    {
        Platform.runLater(() -> controller.upgradeListUsers(c.toString()));
    }

    /**
     * Remove the user from the list view
     * @param c the client to delete
     */
    public void removeUser(Client c)
    {
        if (c != null)
        {
            Platform.runLater(() -> controller.removeUser(c.toString()));
        }
    }

    /**
     * True if is on, false otherwise
     * 
     * @return boolean
     */
    public boolean isOn()
    {
        return on;
    }

    /**
     * Close the app
     */
    public void close()
    {
        on = false;

        primaryStage.close();
    }

    /**
     * Get the primary stage of the application
     * 
     * @return Stage
     */
    public static Stage getPrimaryStage()
    {
        return primaryStage;
    }
}