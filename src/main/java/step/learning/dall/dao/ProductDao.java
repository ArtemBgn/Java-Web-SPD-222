package step.learning.dall.dao;

import com.google.inject.Inject;
import step.learning.dall.dto.Product;
import step.learning.dall.dto.User;
import step.learning.services.db.DbService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductDao {

    private final DbService dbService;
    @Inject
    public ProductDao(DbService dbService) {
        this.dbService = dbService;
    }
    public List<Product> getList(int skip, int take) {
        // skip, take - основа пагінації - поділу на сторінки
        List<Product> result = new ArrayList<>();
        String sql = String.format("SELECT * FROM Products LIMIT %d, %d",
                skip, take);
        try(Statement statement = dbService.getConnection().createStatement())
        {
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                result.add(new Product(resultSet));
            }
        }
        catch (SQLException ex) {
            System.err.println( ex.getMessage() );
            System.out.println( sql );
        }

        return result;
    }
    public boolean verificToken(String token) {
        String sql = "SELECT t.* FROM Tokens t WHERE t.token_expires > CURRENT_TIMESTAMP LIMIT 1";
        try(Statement statement = dbService.getConnection().createStatement())
        {
            ResultSet resultSet2 = statement.executeQuery(sql);
            if(resultSet2.next()) return true;
        }
        catch (SQLException ex) {
            System.err.println( ex.getMessage() );
            System.out.println( sql );
            return false;
        }
        return false;
    }
    public boolean add( Product product ) {
        if( product == null ) return false ;
        if( product.getId() == null ) product.setId( UUID.randomUUID() );
        String sql = "INSERT INTO Products" +
                "(product_id,product_name,product_price,product_description,product_image, product_category) " +
                "VALUES(?,?,?,?,?,?)";
        try( PreparedStatement prep = dbService.getConnection().prepareStatement(sql) ) {
            prep.setString( 1, product.getId().toString() );   // у JDBC відлік від 1
            prep.setString( 2, product.getName() );
            prep.setDouble( 3, product.getPrice() );
            prep.setString( 4, product.getDescription() );
            prep.setString( 5, product.getImage() );
            prep.setString( 6, product.getCategory() );
            prep.executeUpdate();
            return true;
        }
        catch (SQLException ex) {
            System.err.println( ex.getMessage() );
            System.out.println( sql );
            return false ;
        }
    }
}
