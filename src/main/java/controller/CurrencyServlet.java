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

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    CurrencyService service = new CurrencyService();
    GsonShaper gsonShaper = new GsonShaper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getPathInfo().replaceAll("/", "");
        Currency currency = null;
        try {
            currency = service.getCurrencyByCode(code);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        gsonShaper.flashCurrencyAsGson(currency, resp);
    }
}
