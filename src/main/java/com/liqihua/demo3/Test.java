package com.liqihua.demo3;

public class Test {

    public static void main(String[] args) {
        RecvC1 r1 = new RecvC1();
        RecvC2 r2 = new RecvC2();
        r1.toRecv();
        r2.toRecv();
        SendC.send();

    }


}
