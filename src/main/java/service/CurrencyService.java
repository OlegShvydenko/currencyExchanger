package service;

import persistence.entity.Currency;
import persistence.repository.CurrencyRepository;

import java.sql.SQLException;
import java.util.List;

public class CurrencyService {
    CurrencyRepository repository;

    public CurrencyService(CurrencyRepository repository) {
        this.repository = repository;
    }
    public CurrencyService() {
        this.repository = new CurrencyRepository();
    }

    public List<Currency> getAllCurrencies() throws SQLException {
        return repository.getAllCurrencies();
    }

    public Currency getCurrencyByCode(String code) throws SQLException {
        return repository.getCurrencyByCode(code);
    }

    public Currency addNewCurrency(Currency currency) throws SQLException {
        return repository.addNewCurrency(currency);
    }
}
