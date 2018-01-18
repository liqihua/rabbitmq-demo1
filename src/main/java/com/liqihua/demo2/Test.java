package com.liqihua.demo2;

public class Test {
	private String aa;


	public static void main(String[] args) {
		RecvB rec1 = new RecvB("aa");
		RecvB rec2 = new RecvB("bb");
		rec1.toRecv();
		rec2.toRecv();
		SendB sender = new SendB();
		sender.send();


	}
}
