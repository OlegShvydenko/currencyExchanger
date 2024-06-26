package controller;

import dto.Message;
import mapper.RequestMapper;
import persistence.entity.ExchangeRate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;
import util.JsonConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    JsonConverter jsonConverter;
    ExchangeRateService service;
    RequestMapper requestMapper;

    public ExchangeRatesServlet(JsonConverter jsonConverter, ExchangeRateService service, RequestMapper requestMapper) {
        this.jsonConverter = jsonConverter;
        this.service = service;
        this.requestMapper = requestMapper;
    }
    public ExchangeRatesServlet() {
        this.jsonConverter = new JsonConverter();
        this.service = new ExchangeRateService();
        this.requestMapper = new RequestMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ExchangeRate> exchangeRates = null;
        try {
            exchangeRates = service.getAllExchangeRates();
            jsonConverter.writeJsonToResponse(exchangeRates, resp);
        } catch (SQLException e) {
            jsonConverter.writeErrorToResponse( 500, new Message("База данных недоступна"), resp);
        } catch (Exception e) {
            jsonConverter.writeErrorToResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    new Message("Сервер недоступен"), resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRate exchangeRate = null;
        try {
            exchangeRate =
                    service.addNewExchangeRate(requestMapper.mapCode(req.getParameter("baseCurrencyCode")),
                                               requestMapper.mapCode(req.getParameter("targetCurrencyCode")),
                                               requestMapper.mapRate(req));
            jsonConverter.writeJsonToResponse(exchangeRate, resp);
        } catch (IllegalArgumentException e) {
            jsonConverter.writeErrorToResponse( 400,
                    new Message("Отсутствует нужное поле формы"), resp);
        } catch (SQLException e) {
            if (e.getErrorCode() == 19 && e.getMessage().toUpperCase().contains("[SQLITE_CONSTRAINT_NOTNULL]")) {
                jsonConverter.writeErrorToResponse(404,
                        new Message("Одна (или обе) валюта из валютной пары не существует в БД"), resp);
                return;
            }
            if (e.getErrorCode() == 19) {
                jsonConverter.writeErrorToResponse(409,
                        new Message("Валютная пара с таким кодом уже существует"), resp);
            } else {
                jsonConverter.writeErrorToResponse( 500, new Message("База данных недоступна"), resp);
        }
        } catch (Exception e) {
            jsonConverter.writeErrorToResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    new Message("Сервер недоступен"), resp);
        }
    }
}
