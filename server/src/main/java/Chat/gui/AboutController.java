package Chat.gui;

import javafx.fxml.FXML;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class AboutController 
{
    @FXML
    void handleCopyToClipboard(MouseEvent event) 
    {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        content.putString("https://github.com/J-Dany/Chat");
        clipboard.setContent(content);
    }
}