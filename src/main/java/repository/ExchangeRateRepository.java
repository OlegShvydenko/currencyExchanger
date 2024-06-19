package repository;

import util.DbConnector;
import entity.Currency;
import entity.ExchangeRate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateRepository {
    DbConnector dbConnector = new DbConnector();
    Connection connection = dbConnector.getConnection();

    public List<ExchangeRate> getAllExchangeRates() throws SQLException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        String sql = " SELECT exchange_rates.id, base.*, target.*, exchange_rates.rate " +
                "FROM exchange_rates " +
                "JOIN currencies base ON exchange_rates.baseCurrencyId = base.id " +
                "JOIN currencies target ON exchange_rates.targetCurrencyId = target.id";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                exchangeRates.add(getExchangeRateFromQuery(rs));
            }
            return exchangeRates;
        }

    }

    public ExchangeRate getExchangeRateByCode(String baseCurrencyCode, String targetCurrencyCode)
            throws SQLException {
        ExchangeRate exchangeRate = null;
        String sql = " SELECT exchange_rates.id, base.*, target.*, exchange_rates.rate " +
                "FROM exchange_rates " +
                "JOIN currencies base on exchange_rates.baseCurrencyId = base.id " +
                "JOIN currencies target on exchange_rates.targetCurrencyId = target.id " +
                "WHERE base.code = ? AND target.code = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, baseCurrencyCode);
            statement.setString(2, targetCurrencyCode);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                exchangeRate = getExchangeRateFromQuery(rs);
            }
        }
        return exchangeRate;
    }

    private ExchangeRate getExchangeRateFromQuery(ResultSet resultSet) throws SQLException {
        int exchangeRateId = resultSet.getInt(1);

        int currencyId = resultSet.getInt(2);
        String currencyCode = resultSet.getString(3);
        String currencyFullName = resultSet.getString(4);
        String currencySign = resultSet.getString(5);
        Currency baseCurrency = new Currency(currencyId, currencyCode, currencyFullName, currencySign);

        currencyId = resultSet.getInt(6);
        currencyCode = resultSet.getString(7);
        currencyFullName = resultSet.getString(8);
        currencySign = resultSet.getString(9);
        Currency targetCurrency = new Currency(currencyId, currencyCode, currencyFullName, currencySign);

        double rate = resultSet.getDouble(10);

        return new ExchangeRate(exchangeRateId, baseCurrency, targetCurrency, rate);
    }

    public ExchangeRate addNewExchangeRate(String baseCurrencyCode, String targetCurrencyCode, double rate)
            throws SQLException {
        String sql = "INSERT INTO exchange_rates (baseCurrencyId, targetCurrencyId, rate) " +
                "VALUES ((SELECT id FROM currencies WHERE code=?), " +
                "(SELECT id FROM currencies WHERE code=?), ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, baseCurrencyCode);
            statement.setString(2, targetCurrencyCode);
            statement.setDouble(3, rate);
            statement.executeUpdate();
            return getExchangeRateByCode(baseCurrencyCode, targetCurrencyCode);
        }
    }

    public ExchangeRate changeExistingRate(String baseCurrencyCode, String targetCurrencyCode, double rate)
            throws SQLException {
        String sql = "UPDATE exchange_rates " +
                "SET rate = ? " +
                "WHERE (SELECT id FROM currencies WHERE code=?) = baseCurrencyId " +
                "AND (SELECT id FROM currencies WHERE code=?) = targetCurrencyId ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, rate);
            statement.setString(2, baseCurrencyCode);
            statement.setString(3, targetCurrencyCode);
            statement.executeUpdate();
        }
        return getExchangeRateByCode(baseCurrencyCode, targetCurrencyCode);
    }

    public double getRate(String from, String to) throws SQLException {
        double rate = 0;
        rate = getRateByCode(from, to);
        if (rate != 0) return rate;
        rate = 1.0 / getRateByCode(to, from);
        if (rate != 0 && getRateByCode(to, from) != 0) return rate;
        double rateFromUSD = getRateByCode("USD", from);
        double rateTOUSD = getRateByCode("USD", to);
        if(rateFromUSD != 0) return rateTOUSD / rateFromUSD;
        return 0;
    }

    private double getRateByCode(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        double rate = 0;
        String sql = " SELECT rate " +
                "FROM exchange_rates " +
                "WHERE (SELECT id FROM currencies WHERE code = ?) = baseCurrencyId  " +
                "AND (SELECT id FROM currencies WHERE code = ?) = targetCurrencyId";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, baseCurrencyCode);
            statement.setString(2, targetCurrencyCode);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                rate = rs.getDouble(1);
            }
        }
        return rate;
    }
}
