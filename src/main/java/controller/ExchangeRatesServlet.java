package controller;

import entity.ExchangeRate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;
import util.GsonShaper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    GsonShaper gsonShaper = new GsonShaper();
    ExchangeRateService service = new ExchangeRateService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ExchangeRate> exchangeRates = null;
        try {
            exchangeRates = service.getAllExchangeRates();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        gsonShaper.flashListOfExchangeRatesAsGson(exchangeRates, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        double rate = Double.parseDouble(req.getParameter("rate"));
        ExchangeRate exchangeRate = null;
        try {
            exchangeRate = service.addNewExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        gsonShaper.flashExchangeRateAsGson(exchangeRate, resp);
    }
}
