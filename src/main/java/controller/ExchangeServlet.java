package controller;

import entity.Currency;
import entity.ExchangeRate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;
import service.ExchangeRateService;
import util.GsonShaper;
import util.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    ExchangeRateService exchangeRateService = new ExchangeRateService();
    CurrencyService currencyService = new CurrencyService();
    GsonShaper gsonShaper = new GsonShaper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        double amount = Double.parseDouble(req.getParameter("amount"));
        Map<String, Object> map = null;
        try {
            map = exchangeRateService.getRateAsTreeMap(from, to, amount);
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        if (map == null) {
            gsonShaper.printCurrencyException(resp);
            return;
        }
        gsonShaper.flashExchangeAmount(map, resp);
    }
}
