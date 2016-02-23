package com.stone;

import java.io.File;
import java.util.ArrayList;

public class DeleteFile {
//	public static void main(String[] args) {
//		String filePath = "D:\\project\\hdwl_wms\\�ʹ�WMS���򱸷�2015-01-07";//Ҫɾ��.svn��·��
//		File file = new File(filePath);
//		ArrayList<File> allFiles = new ArrayList<File>();
//		each(allFiles,file.listFiles());
//		
//		// ɾ��
////		for (File i : allFiles) {
////			if (i.isDirectory()) {
////				System.out.println(delAllFile(i.getAbsolutePath()));
////			}
////		}
//	}

	public static void each(ArrayList<File> allFiles,File[] every) {
		
		for (File i : every) {
			
			if(i.isDirectory()){
				if (i.getName().equals(".svn")) {
					System.out.println(i.getAbsolutePath()+"--"+delAllFile(i.getAbsolutePath()));
//					allFiles.add(i);
				}else{
					each(allFiles, i.listFiles());
				}
			}
			
		}
		
	}

	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		if(tempList.length==0){
			new File(path).delete();
			return true;
		}
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// ��ɾ���ļ���������ļ�
				delFolder(path + "/" + tempList[i]);// ��ɾ�����ļ���
				flag = true;
			}
		}
		return flag;
	}

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // ɾ����������������
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // ɾ�����ļ���
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
