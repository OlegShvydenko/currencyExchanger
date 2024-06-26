package util;

public class Validator {

    public boolean checkCode(String code) {
        return code.length() == 3;
    }

    public boolean checkCodes(String codes) {
        return codes.length() == 6;
    }
    public boolean checkString(String string){
        return string != "" && string != null;
    }

}
