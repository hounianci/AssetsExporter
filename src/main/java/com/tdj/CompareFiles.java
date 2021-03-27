package com.tdj;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CompareFiles {
    public static void main(String[] args) {
        File goodDir = new File("C:\\workspace\\zs");
        File badDir = new File("C:\\Users\\EDZ\\Downloads\\zspms-pcgw-P36417A-0225\\assets\\resource\\matrix");
        Map<Long, String> map = new HashMap<>();
        for(File f : goodDir.listFiles()){
            map.put(f.length(), f.getName());
        }
        for(File f : badDir.listFiles()){
            if(map.containsKey(f.length()-70)){
                System.out.println(f.length());
                System.out.println(map.get(f.length()-70));
                System.out.println(f.getName());
                System.out.println("----");
            }
        }
    }
}
