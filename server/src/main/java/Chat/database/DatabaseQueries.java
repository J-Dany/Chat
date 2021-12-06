package Chat.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Base64;
import java.util.HashMap;
import com.google.common.hash.Hashing;
import Chat.server.Client;
import Chat.server.Server;
import Chat.server.pojo.Friend;
import com.google.common.base.Charsets;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class DatabaseQueries 
{
    private static Connection connection = DatabaseConnection.getConnection();    

    /**
     * Check if the user exists and the credentials are correct
     * @param client
     * @param username
     * @param password not encrypted password
     * @return
     */
    public static boolean userLogin(Client client, String username, String password) throws Exception
    {
        String query = "" +
"SELECT id_user, username, password " + 
"FROM users WHERE username = '" + username + "'";

        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet result = stmt.executeQuery(query);

        int num_rows = 0;
        if (result.last())
        {
            num_rows = result.getRow();
            result.beforeFirst();
        }

        String passwd = Base64
            .getEncoder()
            .encodeToString(
                Hashing
                    .sha256()
                    .hashString(password, Charsets.UTF_8)
                    .asBytes()
            );

        if (result.next() && num_rows == 1
            && 
            result.getString("username").equals(username) && result.getString("password").equals(passwd))
        {
            client.setId(result.getInt("id_user"));
            client.setUsername(username);

            return true;
        }

        return false;
    }

    /**
     * Get the user friends
     * @param client
     * @return
     * @throws SQLException
     */
    public static HashMap<String, Friend> getUserFriends(Client client) throws SQLException
    {
        HashMap<String, Friend> friends = new HashMap<>();

        String query = "" +
"select u.id_friend, u.friend friend_username, u.photo, u.friend_name, u.friend_surname, " +
"	m.message, m.content_type, m.language, date_format(m.data, \"%H:%i\") HM " +
"from (" + 
"	select u.id_user id_friend, f.user1 user, u.username friend, u.photo photo, u.name friend_name, u.surname friend_surname " +
"	from friends f " +
"	inner join users u on f.user2 = u.id_user " +
"	where user1 = " + client.getId() +
") u " +
"left join messages m on (m.sender = " + client.getId() + " and m.addresse = u.id_friend) or (m.sender = u.id_friend and m.addresse = " + client.getId() + ") " +
"where m.data = ( " +
"	select max(data) " +
"	from messages " +
"	where " +
"	(m.sender = sender and m.addresse = addresse) " +
"	or " +
"	(m.sender = addresse and m.addresse = sender) " +
") " +
"order by u.friend;";

        Statement stmt = connection.createStatement();

        ResultSet result = stmt.executeQuery(query);

        while (result.next())
        {
            Friend f = new Friend();
            f.setUsername(result.getString("friend_username"));
            f.setName(result.getString("friend_name"));
            f.setSurname(result.getString("friend_surname"));
            f.setPhoto(result.getString("photo"));
            f.setOnline(Server.server.isOnline(result.getString("friend_username")));
            f.setIdFriend(result.getInt("id_friend"));
            f.setLastMessage(result.getString("message"));
            f.setContent(result.getString("content_type"));
            f.setLanguage(result.getString("language"));

            friends.put(result.getString("friend_username"), f);
        }

        return friends;
    }

    /**
     * Insert new message into the database
     * @param message
     * @param data
     * @param clientId
     * @param friendId
     * @param content_type
     * @param language
     * @throws SQLException
     */
    public static void insertNewMessage(String message, String data, int clientId, int friendId, String content_type, String language) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO messages(data, message, id_group, sender, addresse, content_type, language)"
        + " VALUES (?, ?, ?, ?, ?, ?, ?);");

        statement.setString(1, data);
        statement.setString(2, message);
        statement.setNull(3, Types.NULL);
        statement.setInt(4, clientId);
        statement.setInt(5, friendId);
        statement.setString(6, content_type);
        statement.setString(7, language);

        statement.executeUpdate();
    }
}
