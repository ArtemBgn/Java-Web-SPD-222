package step.learning.dall.dao;

import com.google.inject.Inject;
import step.learning.dall.dto.CartItem;
import step.learning.dall.dto.Product;
import step.learning.dall.dto.User;
import step.learning.services.db.DbService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CartDao {
    private final DbService dbService;
    @Inject
    public CartDao(DbService dbService) {
        this.dbService = dbService;
    }
    public List<CartItem> getCart(String userId) {
        List<CartItem> result = new ArrayList<>();
        if(userId!=null) {
            String sql = String.format(
                    "SELECT * FROM carts c JOIN carts_details cd ON c.cart_id = cd.cart_id WHERE c.cart_user = '%s'", userId);
            try(Statement statement = dbService.getConnection().createStatement()) {
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    result.add(new CartItem(resultSet));
                }
            }
            catch (SQLException ex) {
                System.err.println(ex.getMessage());
                System.out.println(sql);
            }
        }
        return result;
    }
    public void add(String userId, String productId, int cnt) {
        // Шукаємо чи є у користувача відкритий кошик, якщо немає, то створюємо
        // перевіряємо чи є у кошику такий товар, якщо є, то збільшуємо кількість,
        // якщо немає, то створюємо (додаємо)
        String sql = String.format(
                "SELECT cart_id FROM carts WHERE cart_user='%s' AND cart_status=0 ",
                userId);
        try(Statement statement = dbService.getConnection().createStatement()) {
            String cartId = null;
            ResultSet res = statement.executeQuery(sql);
            if(res.next()) { // є відкритий кошик
                cartId = res.getString(1);
            }
            else { // немає відкритого кошику
                cartId = UUID.randomUUID().toString();
                sql = String.format(
                        "INSERT INTO carts(cart_id, cart_user, cart_date, cart_status) VALUES('%s', '%s', CURRENT_TIMESTAMP, 0)",
                        cartId, userId);
                statement.executeUpdate(sql);
            }
            // cartId - id кошику чи нового чи старого
            // перевіряємо чи є товар у кошику
            sql = String.format(
                    "SELECT cart_dt_cnt FROM carts_details WHERE cart_id = '%s' AND product_id = '%s'",
                    cartId, productId);
            res = statement.executeQuery(sql);
            if(res.next()) { // є такий товар
                cnt += res.getInt(1);
                sql = String.format("UPDATE carts_details SET cart_dt_cnt = '%d' WHERE cart_id = '%s' AND product_id = '%s'",
                        cnt, cartId, productId);
            }
            else { // немає  такого товару. Створюємо(додаємо)
                sql = String.format("INSERT INTO carts_details(cart_dt_id, cart_id, product_id, cart_dt_cnt) VALUES(UUID(), '%s', '%s', '%d') ",
                        cartId, productId, cnt);
            }
            statement.executeUpdate(sql);
        }
        catch (SQLException ex) {
            System.err.println( ex.getMessage() );
            System.out.println( sql );
        }
    }
    public void deleteProduct(String userId, String productId) {
        // Шукаємо чи є у користувача відкритий кошик, якщо немає, то виходимо
        String sql = String.format(
                "SELECT cart_id FROM carts WHERE cart_user='%s' AND cart_status=0 ",
                userId);
        try(Statement statement = dbService.getConnection().createStatement()) {
            String cartId = null;
            ResultSet res = statement.executeQuery(sql);
            if(res.next()) { // є відкритий кошик
                cartId = res.getString(1);
            }
            else { // немає відкритого кошику
                return;
            }
            // перевіряємо чи є товар у кошику,
            // якщо є - видаляємо
            sql = String.format(
                    "SELECT cart_dt_cnt FROM carts_details WHERE cart_id = '%s' AND product_id = '%s'",
                    cartId, productId);
            res = statement.executeQuery(sql);
            if(res.next()) { // є такий товар
                sql = String.format("DELETE FROM carts_details WHERE cart_id = '%s' AND product_id = '%s'",
                        cartId, productId);
            }
            else { // немає  такого товару
                return;
            }
            statement.executeUpdate(sql);
        }
        catch (SQLException ex) {
            System.err.println( ex.getMessage() );
            System.out.println( sql );
        }
    }
}
