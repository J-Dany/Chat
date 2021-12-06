package Chat.gui;

import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class ExitingDataController 
{
    @FXML
    private TreeView<String> tree;

    public void loadData(String data)
    {
        if (data == null)
        {
            return;
        }

        JSONObject json = new JSONObject(data);

        TreeItem<String> rootItem = new TreeItem<>("json");
        rootItem.setExpanded(true);

        json.keys().forEachRemaining(key -> insertNode(rootItem, key, json.get(key)));

        tree.setRoot(rootItem);
    }

    private void insertNode(TreeItem<String> rootNode, String key, Object value)
    {
        TreeItem<String> item = new TreeItem<String>(key);

        if (value instanceof String || value instanceof Integer || value instanceof Boolean)
        {
            item.setValue(key + ": " + String.valueOf(value));
        }
        else if (value instanceof JSONObject)
        {
            JSONObject obj = (JSONObject)value;
            obj.keys().forEachRemaining(k -> insertNode(item, k, obj.get(k)));
        }
        else if (value instanceof JSONArray)
        {
            JSONArray array = (JSONArray) value;
            Iterator<Object> iterator = array.iterator();

            for (int i = 0; iterator.hasNext(); ++i)
            {
                insertNode(item, Integer.toString(i), iterator.next());
            }
        }

        rootNode.getChildren().add(item);
    }
}
