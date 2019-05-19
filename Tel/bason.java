package com.qhit.tests;

/**
 * Created by HP on 2019/5/9.
 */
public class bason {
    private String code;
    private String msg;
    private String obj;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "bason{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", obj='" + obj + '\'' +
                '}';
    }
}
