package com.hpe.utils.email;

import java.util.Random;

public class CodeRandom {
	public static String getNumber() {
		// TODO Auto-generated method stub
		String str="0123456789";
		String number="";
		Random r=new Random();
		for(int i=0;i<6;i++){
			number+=str.charAt(r.nextInt(str.length()));
		}
		return number;
	}

}
