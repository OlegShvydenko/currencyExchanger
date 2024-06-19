package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Currency;
import entity.ExchangeRate;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GsonShaper {
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.setPrettyPrinting().create();

    public void flashListOfCurrenciesAsGson(List<Currency> currencies, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(gson.toJson(currencies));
        out.flush();
    }

    public void flashCurrencyAsGson(Currency currency, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(gson.toJson(currency));
        out.flush();
    }

    public void flashExchangeRateAsGson(ExchangeRate exchangeRate, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(gson.toJson(exchangeRate));
        out.flush();
    }

    public void flashListOfExchangeRatesAsGson(List<ExchangeRate> exchangeRates, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(gson.toJson(exchangeRates));
        out.flush();
    }

    public void flashExchangeAmount(Map<String, Object> map, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(gson.toJson(map));
        out.flush();
    }
    public void printCurrencyException(HttpServletResponse resp) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("message", "Валюта не найдена");
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(gson.toJson(map));
        out.flush();
    }
}
