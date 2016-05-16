package com.guohuai.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdminData {

	public static Map<String, String> loadAdmin() throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		File f = new File("C:\\Users\\Arthur\\Desktop\\新建文件夹\\用户.txt");
		Scanner in = new Scanner(f, "gb2312");
		while (in.hasNextLine()) {
			String line = in.nextLine();
			if (!line.equals("")) {
				String[] u = line.split("\t");
				map.put(u[0], u[1]);
				map.put(u[1], u[0]);
			}
		}
		in.close();
		return map;
	}

}
