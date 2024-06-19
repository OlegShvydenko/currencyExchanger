package service;

import entity.Currency;
import repository.CurrencyRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyService {
    CurrencyRepository repository = new CurrencyRepository();

    public List<Currency> getAllCurrencies() throws SQLException {
        return repository.getAllCurrencies();
    }

    public Currency getCurrencyByCode(String code) throws SQLException {
        return repository.getCurrencyByCode(code);
    }

    public Currency setNewCurrency(String code, String fullName, String sign) throws SQLException {
        Currency currency = repository.setNewCurrency(code, fullName, sign);
        return currency;
    }
}
