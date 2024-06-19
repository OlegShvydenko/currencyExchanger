package service;

import entity.Currency;
import entity.ExchangeRate;
import repository.CurrencyRepository;
import repository.ExchangeRateRepository;
import util.Pair;

import java.sql.SQLException;
import java.util.*;

public class ExchangeRateService {
    ExchangeRateRepository repository = new ExchangeRateRepository();
    CurrencyRepository currencyRepository = new CurrencyRepository();

    public List<ExchangeRate> getAllExchangeRates() throws SQLException {
        return repository.getAllExchangeRates();
    }

    public ExchangeRate getExchangeRateByCode(String codes)
            throws SQLException {
        String baseCurrencyCode = codes.substring(0, 3);
        String targetCurrencyCode = codes.substring(3, 6);
        return repository.getExchangeRateByCode(baseCurrencyCode, targetCurrencyCode);
    }

    public ExchangeRate addNewExchangeRate(String baseCurrencyCode, String targetCurrencyCode, double rate)
            throws SQLException {
        return repository.addNewExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
    }
    public ExchangeRate changeExistingRate(String codes, double rate) throws SQLException {
        String baseCurrencyCode = codes.substring(0, 3);
        String targetCurrencyCode = codes.substring(3, 6);
        return repository.changeExistingRate(baseCurrencyCode, targetCurrencyCode, rate);
    }

    public Map<String, Object> getRateAsTreeMap(String from, String to, double amount) throws SQLException {
        Map<String, Object> map = new LinkedHashMap<>();
        Currency baseCurrency = currencyRepository.getCurrencyByCode(from);
        if (baseCurrency == null) return null;
        Currency targetCurrency = currencyRepository.getCurrencyByCode(to);
        if (targetCurrency == null) return null;
        double rate = repository.getRate(from, to);
        if (rate == 0) return null;
        map.put("baseCurrency", baseCurrency);
        map.put("targetCurrency", targetCurrency);
        map.put("rate", rate);
        map.put("amount", amount);
        map.put("convertedAmount", getConvertedAmount(rate, amount));
        return map;
    }

    public double getConvertedAmount(double rate, double amount){
        return rate * amount;
    }
}
