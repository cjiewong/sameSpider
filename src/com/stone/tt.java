package com.stone;

public class tt {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String print_string = "^FO30,30^A@N,20,20,B:966.ARF^FD������׷�������Ŀ��ֻ�ж�����һһ�����֡���^FS";
        
        try {
			print_string = new String( print_string.toString().getBytes( "utf-8" ), "gbk");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println(print_string);
	}
}
