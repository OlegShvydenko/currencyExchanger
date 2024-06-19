package repository;

import util.DbConnector;
import entity.Currency;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRepository {
    DbConnector dbConnector = new DbConnector();
    Connection connection = dbConnector.getConnection();

    public List<Currency> getAllCurrencies() throws SQLException {
        List<Currency> currencies = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM currencies");
            while (rs.next()) {
                int id = rs.getInt("id");
                String code = rs.getString("code");
                String fullName = rs.getString("fullName");
                String sign = rs.getString("sign");
                Currency currency = new Currency(id, code, fullName, sign);
                currencies.add(currency);
            }
        }
        return currencies;

    }

    public Currency getCurrencyByCode(String code) throws SQLException {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM currencies WHERE code = ?");
            statement.setString(1, code);
            ResultSet rs = statement.executeQuery();
            int id = rs.getInt("id");
            String fullName = rs.getString("fullName");
            String sign = rs.getString("sign");
            return new Currency(id, code, fullName, sign);
    }

    public Currency setNewCurrency(String code, String fullName, String sign) throws SQLException{
        Currency currency = new Currency(code, fullName, sign);
        String sql = "INSERT INTO currencies(code, fullName, sign) VALUES(?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getName());
            statement.setString(3, currency.getSign());
            statement.executeUpdate();
        return currency;
    }

}
