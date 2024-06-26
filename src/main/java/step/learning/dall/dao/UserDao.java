package step.learning.dall.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dall.dto.User;
import step.learning.services.db.DbService;

import java.sql.*;
import java.util.UUID;

@Singleton
public class UserDao {
    private final DbService dbService;
    @Inject
    public UserDao(DbService dbService) {
        this.dbService = dbService;
    }
    public boolean exitUser(String token) {
        Timestamp time_expires = new Timestamp(new java.util.Date().getTime());
        updateTimeToken(token, time_expires);
        /*
        String sql = "UPDATE Tokens SET token_expires = ? WHERE token_id = ${token}";
        try( PreparedStatement prep = dbService.getConnection().prepareStatement(sql))
        {
            prep.setTimestamp(1, new Timestamp(new java.util.Date().getTime()-1));
            prep.executeUpdate();
            return true;
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println(sql);
        }*/
        return true;
    }
    public String generateToken(User user) {
        // перевіряємо чи є валідний токен
        String sqlGetTokenByUser = "SELECT t.* FROM Tokens t WHERE t.user_id = ? AND t.token_expires > CURRENT_TIMESTAMP LIMIT 1";
        try( PreparedStatement prep = dbService.getConnection().prepareStatement(sqlGetTokenByUser))
        {
            prep.setString(1, user.getId().toString());
            ResultSet res = prep.executeQuery();
            if( res.next()) { // якщо є дані - token валідний
                String token = res.getString("token_id");
                Timestamp time_expires = res.getTimestamp("token_expires");
                updateTimeToken(token, time_expires);
                return token;
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println(sqlGetTokenByUser);
        }

        String sql = "INSERT INTO Tokens(token_id, user_id, token_expires) VALUES(?,?,?)";
        try( PreparedStatement prep = dbService.getConnection().prepareStatement(sql))
        {
            String token = UUID.randomUUID().toString();
            prep.setString(1, token);
            prep.setString(2, user.getId().toString());
            prep.setTimestamp(3, new Timestamp(new java.util.Date().getTime()+60*5*1000));
            prep.executeUpdate();
            return token;
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println(sql);
        }
        return null;
    }
    private void updateTimeToken(String tokenId, Timestamp time_expires) {
        String sql = "UPDATE Tokens SET token_expires = ? WHERE token_id = ?";
        try( PreparedStatement prep = dbService.getConnection().prepareStatement(sql))
        {
            prep.setTimestamp(1, new Timestamp(time_expires.getTime()+60*5*500));
            prep.setString(2, tokenId);
            prep.executeUpdate();
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println(sql);
        }
    }
    public User getUserByToken( String token ) {
        String sql = "SELECT t.*, u.* " +
                "FROM Tokens t JOIN Users u ON t.user_id = u.user_id " +
                "WHERE t.token_id = ? AND t.token_expires > CURRENT_TIMESTAMP " +//AND t.token_expires > CURRENT_TIMESTAMP при формуванні часу домножити на 1000
                "LIMIT 1";
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, token);
            ResultSet res = prep.executeQuery();
            if( res.next()) { //якщо є дані (значить користувача знайдено)
                Timestamp time_expires = new Timestamp(new java.util.Date().getTime()+60*5*500); //res.getTimestamp("token_expires");
                updateTimeToken(token, time_expires);
                return User.fromResultSet( res );
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println(sql);
        }
        return null;
    }
    public User getUserByEmail( String email ) {
        String sql = "SELECT u. * FROM Users u WHERE u.user_email = ?";
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, email);
            ResultSet res = prep.executeQuery();
            if( res.next()) { //якщо є дані (значить користувача знайдено)
                return User.fromResultSet( res );
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println(sql);
        }
        return null;
    }
    public boolean registerUser(User user) {
        if(user == null) return false;
        if(user.getId() == null) user.setId(UUID.randomUUID());
        String sql = "INSERT INTO Users" +
                "(user_id, user_name, user_email, user_avatar, user_salt, user_dk)" +
                "VALUES(?,?,?,?,?,?)";
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, user.getId().toString()); // у JDBS відлік від 1
            prep.setString(2, user.getName());
            prep.setString(3, user.getEmail());
            prep.setString(4, user.getAvatar());
            prep.setString(5, user.getSalt());
            prep.setString(6, user.getDerivedKey());
            prep.executeUpdate();
            return true;
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println(sql);
            return false;
        }
    }
    public boolean installTable() {
        String sql = "CREATE TABLE Users (" +
                "user_id CHAR(36) PRIMARY KEY DEFAULT(UUID())," +
                "user_name VARCHAR(64) NOT NULL," +
                "user_email VARCHAR(128) NOT NULL," +
                "user_avatar VARCHAR(64) NOT NULL," +
                "user_salt VARCHAR(32) NOT NULL," +
                "user_dk VARCHAR(32) NOT NULL," +
                "user_created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "user_deleted DATETIME NULL" +
                ")ENGINE = INNODB, DEFAULT CHARSET = utf8mb4";
        try(Statement statement = dbService.getConnection().createStatement()) {
            statement.executeUpdate( sql );
            return true;
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println(sql);
            return false;
        }
    }
}
