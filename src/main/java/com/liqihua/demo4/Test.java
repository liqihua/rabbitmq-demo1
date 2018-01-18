package com.liqihua.demo4;

public class Test {

    public static void main(String[] args) {
        RecvDinfo info = new RecvDinfo();
        RecvDwarn warn = new RecvDwarn();
        RecvDerror error = new RecvDerror();
        info.toRecv();
        warn.toRecv();
        error.toRecv();
        SendD.send();

    }


}
