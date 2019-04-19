package com.hpe.entity;

import java.util.Comparator;

public class FilesSort implements Comparator<FileInfo>{

	public int compare(FileInfo o1, FileInfo o2) {
		if(!o1.isIsdir()&&o2.isIsdir()){
			return 1;
		}else if(o1.isIsdir()&&!o2.isIsdir()){
			return -1;
		}
		if (o1.getName().compareToIgnoreCase(o2.getName())>0) {
            return 1;
        } else if (o1.getName().compareToIgnoreCase(o2.getName())==0) {
            return 0;
        } else {
            return -1;
        }
	}
}




