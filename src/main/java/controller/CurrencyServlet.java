package controller;

import dto.Message;
import mapper.RequestMapper;
import persistence.entity.Currency;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;
import util.JsonConverter;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    CurrencyService currencyService;
    JsonConverter jsonConverter;
    RequestMapper requestMapper;

    public CurrencyServlet(CurrencyService currencyService, JsonConverter jsonConverter, RequestMapper requestMapper) {
        this.currencyService = currencyService;
        this.jsonConverter = jsonConverter;
        this.requestMapper = requestMapper;
    }

    public CurrencyServlet() {
        this.currencyService = new CurrencyService();
        this.jsonConverter = new JsonConverter();
        this.requestMapper = new RequestMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Currency currency = null;
        try {
            currency = currencyService.getCurrencyByCode(requestMapper.mapCode(req));
            jsonConverter.writeJsonToResponse(currency, resp);
        } catch (IllegalArgumentException e) {
            jsonConverter.writeErrorToResponse(400, new Message(
                    "Код валюты отсутствует в адресе"), resp);
        } catch (NullPointerException e) {
            jsonConverter.writeErrorToResponse(404, new Message(
                    "Валюта не найдена"), resp);
        } catch (SQLException e) {
            jsonConverter.writeErrorToResponse( 500, new Message("База данных недоступна"), resp);
        } catch (Exception e) {
            jsonConverter.writeErrorToResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new Message(
                    "Сервер недоступен"), resp);
        }
    }
}
