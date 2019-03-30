package com.auto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
	/*public static void main(String[] args) {
		String aa = "222";
		String bb = "a12";
		System.out.println(aa.matches("\\d+"));
		System.out.println(bb.matches("\\d+"));
	}*/
	
	public static void main(String args[]) {
	       int i= compare_date("1999-12", "1999-11");
	       System.out.println("WWW"+i);
	    }

	public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
}
