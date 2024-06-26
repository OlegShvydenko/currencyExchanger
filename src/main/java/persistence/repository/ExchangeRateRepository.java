package persistence.repository;

import mapper.DtoMapper;
import persistence.DbConnector;
import persistence.entity.Currency;
import persistence.entity.ExchangeRate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateRepository {
    DbConnector dbConnector;
    Connection connection;
    DtoMapper dtoMapper;

    public ExchangeRateRepository(DbConnector dbConnector, Connection connection, DtoMapper dtoMapper) {
        this.dbConnector = dbConnector;
        this.connection = connection;
        this.dtoMapper = dtoMapper;
    }

    public ExchangeRateRepository() {
        this.dbConnector = new DbConnector();
        this.connection = dbConnector.getConnection();
        this.dtoMapper = new DtoMapper();
    }

    public List<ExchangeRate> getAllExchangeRates() throws SQLException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        String sql = " SELECT exchange_rates.id, base.*, target.*, exchange_rates.rate " +
                "FROM exchange_rates " +
                "JOIN currencies base ON exchange_rates.baseCurrencyId = base.id " +
                "JOIN currencies target ON exchange_rates.targetCurrencyId = target.id";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                exchangeRates.add(dtoMapper.mapExchangeRate(rs));
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
                exchangeRate = dtoMapper.mapExchangeRate(rs);
            }
        }
        return exchangeRate;
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
        if (rateFromUSD != 0) return rateTOUSD / rateFromUSD;
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
