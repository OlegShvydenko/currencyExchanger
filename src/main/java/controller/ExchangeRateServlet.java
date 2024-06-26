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

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    ExchangeRateService exchangeRateService;
    JsonConverter jsonConverter;
    RequestMapper requestMapper;

    public ExchangeRateServlet(ExchangeRateService exchangeRateService, JsonConverter jsonConverter, RequestMapper requestMapper) {
        this.exchangeRateService = exchangeRateService;
        this.jsonConverter = jsonConverter;
        this.requestMapper = requestMapper;
    }

    public ExchangeRateServlet() {
        this.exchangeRateService = new ExchangeRateService();
        this.jsonConverter = new JsonConverter();
        this.requestMapper = new RequestMapper();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, res);
            return;
        }
        this.doPatch(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRate exchangeRate = null;
        try {
            exchangeRate = exchangeRateService.getExchangeRateByCode(requestMapper.mapCodes(req));
            if (exchangeRate == null) {
                jsonConverter.writeErrorToResponse(404,
                        new Message("Обменный курс для пары не найден"), resp);
                return;
            }
            jsonConverter.writeJsonToResponse(exchangeRate, resp);
        } catch (IllegalArgumentException e) {
            jsonConverter.writeErrorToResponse(400,
                    new Message("Коды валют пары отсутствуют в адресе"), resp);
        } catch (SQLException e) {
            jsonConverter.writeErrorToResponse(500, new Message("База данных недоступна"), resp);
        } catch (Exception e) {
            jsonConverter.writeErrorToResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    new Message("Сервер недоступен"), resp);
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRate exchangeRate = null;
        try {
            exchangeRate =
                    exchangeRateService.changeExistingRate(requestMapper.mapCodes(req), requestMapper.mapRate(req));
            if (exchangeRate == null) {
                jsonConverter.writeErrorToResponse(404,
                        new Message("Валютная пара отсутствует в базе данных"), resp);
                return;
            }
            jsonConverter.writeJsonToResponse(exchangeRate, resp);

        } catch (IllegalArgumentException e) {
            jsonConverter.writeErrorToResponse(400,
                    new Message("Отсутствует нужное поле формы"), resp);
        } catch (SQLException e) {
            jsonConverter.writeErrorToResponse(500, new Message("База данных недоступна"), resp);
        } catch (Exception e) {
            jsonConverter.writeErrorToResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    new Message("Сервер недоступен"), resp);
        }
    }
}
