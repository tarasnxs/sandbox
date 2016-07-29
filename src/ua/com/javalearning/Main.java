package ua.com.javalearning;

public class Main {
	public static String hello;

	public static void main(String[] args) {
		hello = "Hello World!";
		System.out.println(hello);
		hello = "######################################";
		for (int i = 0; i < 40000; i++) {
			for(int j = 0; j < hello.length(); j++) {
				StringBuilder str = new StringBuilder(hello);
				str.setCharAt(j, ' ');
				System.out.println(str);
				try {
					Thread.sleep(90);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}

}
