package entity;

public class Currency {
    private int id;
    private String name;
    private String code;
    private String sign;

    public Currency(String code, String fullName, String sign) {
        this.name = fullName;
        this.code = code;
        this.sign = sign;
    }

    public Currency(int id, String code, String fullName, String sign) {
        this.id = id;
        this.name = fullName;
        this.code = code;
        this.sign = sign;
    }

    public int getID() {
        return id;
    }

    public void setID(int ID) {
        this.id = ID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
