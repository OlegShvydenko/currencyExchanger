package controller;

import dto.Message;
import persistence.entity.Currency;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mapper.RequestMapper;
import service.CurrencyService;
import util.JsonConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    CurrencyService currencyService;
    JsonConverter jsonConverter;
    RequestMapper requestMapper;

    public CurrenciesServlet(CurrencyService currencyService, JsonConverter jsonConverter, RequestMapper requestMapper) {
        this.currencyService = currencyService;
        this.jsonConverter = jsonConverter;
        this.requestMapper = requestMapper;
    }

    public CurrenciesServlet() {
        this.currencyService = new CurrencyService();
        this.jsonConverter = new JsonConverter();
        this.requestMapper = new RequestMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Currency> currencies = null;
        try {
            currencies = currencyService.getAllCurrencies();
            jsonConverter.writeJsonToResponse(currencies, resp);
        } catch (SQLException e) {
            jsonConverter.writeErrorToResponse( 500, new Message("База данных недоступна"), resp);
        } catch (Exception e) {
            jsonConverter.writeErrorToResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new Message(
                    "Сервер недоступен"), resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Currency currency = null;
        try {
            currency = currencyService.addNewCurrency(requestMapper.mapCurrency(req));
            jsonConverter.writeJsonToResponse(currency, resp);
        } catch (IllegalArgumentException e){
            jsonConverter.writeErrorToResponse(400, new Message(
                    "Отсутствует нужное поле формы"), resp);
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                jsonConverter.writeErrorToResponse(409, new Message(
                        "Валюта с таким кодом уже существует"), resp);
            }
            else jsonConverter.writeErrorToResponse( 500, new Message("База данных недоступна"), resp);
        } catch (Exception e) {
            jsonConverter.writeErrorToResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    new Message("Сервер недоступен"), resp);
        }
    }

}
