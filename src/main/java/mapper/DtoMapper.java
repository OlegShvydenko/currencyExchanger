package mapper;

import jakarta.servlet.http.HttpServletRequest;
import persistence.entity.Currency;
import persistence.entity.ExchangeRate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DtoMapper {
    public Currency mapCurrency(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        if (id == 0) throw new NullPointerException();
        String name = rs.getString("fullName");
        String code = rs.getString("code");
        String sign = rs.getString("sign");
        return new Currency(id, name, code, sign);
    }

    public ExchangeRate mapExchangeRate(ResultSet rs) throws SQLException {
        int exchangeRateId = rs.getInt(1);

        int currencyId = rs.getInt(2);
        String currencyCode = rs.getString(3);
        String currencyFullName = rs.getString(4);
        String currencySign = rs.getString(5);
        Currency baseCurrency = new Currency(currencyId, currencyCode, currencyFullName, currencySign);

        currencyId = rs.getInt(6);
        currencyCode = rs.getString(7);
        currencyFullName = rs.getString(8);
        currencySign = rs.getString(9);
        Currency targetCurrency = new Currency(currencyId, currencyCode, currencyFullName, currencySign);

        double rate = rs.getDouble(10);

        return new ExchangeRate(exchangeRateId, baseCurrency, targetCurrency, rate);
    }
}
