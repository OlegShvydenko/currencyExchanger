package persistence.repository;

import mapper.DtoMapper;
import persistence.DbConnector;
import persistence.entity.Currency;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRepository {
    DbConnector dbConnector;
    Connection connection;
    DtoMapper dtoMapper;

    public CurrencyRepository(DbConnector dbConnector, Connection connection, DtoMapper dtoMapper) {
        this.dbConnector = dbConnector;
        this.connection = connection;
        this.dtoMapper = dtoMapper;
    }

    public CurrencyRepository() {
        this.dbConnector = new DbConnector();
        this.connection = dbConnector.getConnection();
        this.dtoMapper = new DtoMapper();
    }

    public List<Currency> getAllCurrencies() throws SQLException {
        List<Currency> currencies = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM currencies");
            while (rs.next()) {
                currencies.add(dtoMapper.mapCurrency(rs));
            }
        }
        return currencies;

    }

    public Currency getCurrencyByCode(String code) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM currencies WHERE code = ?");
        statement.setString(1, code);
        ResultSet rs = statement.executeQuery();
        return dtoMapper.mapCurrency(rs);
    }

    public Currency addNewCurrency(Currency currency) throws SQLException {
        String sql = "INSERT INTO currencies(code, fullName, sign) VALUES(?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, currency.getCode());
        statement.setString(2, currency.getName());
        statement.setString(3, currency.getSign());
        statement.executeUpdate();
        return currency;
    }

}
