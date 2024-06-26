package controller;

import dto.Message;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mapper.RequestMapper;
import service.ExchangeRateService;
import util.JsonConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    ExchangeRateService exchangeRateService;
    JsonConverter jsonConverter;
    RequestMapper requestMapper;

    public ExchangeServlet(ExchangeRateService exchangeRateService, JsonConverter jsonConverter, RequestMapper requestMapper) {
        this.exchangeRateService = exchangeRateService;
        this.jsonConverter = jsonConverter;
        this.requestMapper = requestMapper;
    }

    public ExchangeServlet() {
        this.exchangeRateService = new ExchangeRateService();
        this.jsonConverter = new JsonConverter();
        this.requestMapper = new RequestMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> map = null;
        try {
            map =
                    exchangeRateService.getRateAsTreeMap(requestMapper.mapCode(req.getParameter("from")),
                                                         requestMapper.mapCode(req.getParameter("to")),
                                                         requestMapper.mapDouble(req.getParameter("amount")));

            jsonConverter.writeJsonToResponse(map, resp);
        } catch (NullPointerException e) {
            jsonConverter.writeErrorToResponse(404, new Message(
                    "Нет такой валютной пары"), resp);
        } catch (IllegalArgumentException e) {
            jsonConverter.writeErrorToResponse(400, new Message(
                    "Данные введены некорректно"), resp);
        } catch (SQLException e) {
            jsonConverter.writeErrorToResponse( 500, new Message("База данных недоступна"), resp);
        } catch (Exception e) {
            jsonConverter.writeErrorToResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new Message(
                    "Сервер недоступен"), resp);
        }
    }
}
