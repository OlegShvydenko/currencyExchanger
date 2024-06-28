package mapper;

import persistence.entity.Currency;
import jakarta.servlet.http.HttpServletRequest;
import util.Validator;

import java.util.Objects;

public class RequestMapper {
    Validator validator;

    public RequestMapper(Validator validator) {
        this.validator = validator;
    }

    public RequestMapper() {
        this.validator = new Validator();
    }

    public Currency mapCurrency(HttpServletRequest req) {
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");
        if (!validator.checkString(code) || !validator.checkString(name) || !validator.checkString(sign) ||
                !validator.checkCode(code)) {
            throw new IllegalArgumentException();
        }
        return new Currency(name, code, sign);
    }

    public String mapCode(HttpServletRequest req) {
        String code = req.getPathInfo().replaceAll("/", "");
        if (code.equals("") || !validator.checkCode(code)) {
            throw new IllegalArgumentException();
        }
        return code;
    }

    public String mapCode(String string) {
        if (string.equals("") || !validator.checkCode(string)) {
            throw new IllegalArgumentException();
        }
        return string;
    }

    public String mapCodes(HttpServletRequest req) {
        String codes = req.getPathInfo().replaceAll("/", "");
        if (codes.equals("") || !validator.checkCodes(codes)) {
            throw new IllegalArgumentException();
        }
        return codes;
    }

    public double mapRate(HttpServletRequest req) {
        double rate;
        try {
            rate = Double.parseDouble(req.getParameter("rate"));
        }
        catch (Exception e){
            throw new IllegalArgumentException();
        }

        return rate;
    }

    public double mapDouble(String string){
        if (Objects.equals(string, "")) throw new IllegalArgumentException();
        return Double.parseDouble(string);
    }
}
