package controller;

import entity.Currency;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;
import util.GsonShaper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    CurrencyService service = new CurrencyService();
    GsonShaper gsonShaper = new GsonShaper();

    public CurrenciesServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Currency> currencies = null;

        try {
            currencies = service.getAllCurrencies();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        gsonShaper.flashListOfCurrenciesAsGson(currencies, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        Currency currency = null;
        try {
            currency = service.setNewCurrency(code, name, sign);
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        gsonShaper.flashCurrencyAsGson(currency, resp);

    }

}
