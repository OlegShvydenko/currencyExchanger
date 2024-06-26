package service;

import persistence.entity.Currency;
import persistence.entity.ExchangeRate;
import persistence.repository.CurrencyRepository;
import persistence.repository.ExchangeRateRepository;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

public class ExchangeRateService {
    ExchangeRateRepository exchangeRateRepository;
    CurrencyRepository currencyRepository;

    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository, CurrencyRepository currencyRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.currencyRepository = currencyRepository;
    }

    public ExchangeRateService() {
        this.exchangeRateRepository = new ExchangeRateRepository();
        this.currencyRepository = new CurrencyRepository();
    }

    public List<ExchangeRate> getAllExchangeRates() throws SQLException {
        return exchangeRateRepository.getAllExchangeRates();
    }

    public ExchangeRate getExchangeRateByCode(String codes)
            throws SQLException {
        String baseCurrencyCode = codes.substring(0, 3);
        String targetCurrencyCode = codes.substring(3, 6);
        return exchangeRateRepository.getExchangeRateByCode(baseCurrencyCode, targetCurrencyCode);
    }

    public ExchangeRate addNewExchangeRate(String baseCurrencyCode, String targetCurrencyCode, double rate)
            throws SQLException {
        return exchangeRateRepository.addNewExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
    }

    public ExchangeRate changeExistingRate(String codes, double rate) throws SQLException {
        String baseCurrencyCode = codes.substring(0, 3);
        String targetCurrencyCode = codes.substring(3, 6);
        return exchangeRateRepository.changeExistingRate(baseCurrencyCode, targetCurrencyCode, rate);
    }

    public Map<String, Object> getRateAsTreeMap(String from, String to, double amount) throws SQLException {
        Map<String, Object> map = new LinkedHashMap<>();
        Currency baseCurrency = currencyRepository.getCurrencyByCode(from);
        if (baseCurrency == null) return null;
        Currency targetCurrency = currencyRepository.getCurrencyByCode(to);
        if (targetCurrency == null) return null;
        double rate = exchangeRateRepository.getRate(from, to);
        if (rate == 0) return null;
        map.put("baseCurrency", baseCurrency);
        map.put("targetCurrency", targetCurrency);
        map.put("rate", rate);
        map.put("amount", amount);
        map.put("convertedAmount", getConvertedAmount(rate, amount));
        return map;
    }

    public double getConvertedAmount(double rate, double amount) {
        return roundToDecimals(rate * amount);
    }
    private double roundToDecimals(double d)
    {
        int temp = (int)(d * Math.pow(10 , 2));
        return ((double)temp)/Math.pow(10 , 2);
    }
}
