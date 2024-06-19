package controller;

import com.google.gson.Gson;
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

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    ExchangeRateService service = new ExchangeRateService();
    GsonShaper gsonShaper = new GsonShaper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, res);
            return; // <----- add this
        }
        this.doPatch(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codes = req.getPathInfo().replaceAll("/", "");
        ExchangeRate exchangeRate = null;
        try {
            exchangeRate = service.getExchangeRateByCode(codes);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        gsonShaper.flashExchangeRateAsGson(exchangeRate, resp);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codes = req.getPathInfo().replaceAll("/", "");
        double rate = Double.parseDouble(req.getParameter("rate"));
        ExchangeRate exchangeRate = null;
        try {
            exchangeRate = service.changeExistingRate(codes, rate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        gsonShaper.flashExchangeRateAsGson(exchangeRate, resp);
    }
}
