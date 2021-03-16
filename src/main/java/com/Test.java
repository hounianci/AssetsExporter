package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Test {
    public static void main(String[] args) {
        File dir = new File("C:\\Users\\EDZ\\Downloads\\109_415c1ad6520697655c1951c40371459f\\assets\\resource");
        String h = "Kuro";
        for(File file : dir.listFiles()){
            try(FileInputStream is = new FileInputStream(file);
                FileOutputStream os = new FileOutputStream("zs/"+file.getName())){
                byte[] head = new byte[4];
                is.read(head);
                if(!new String(head).equals(h)){
                    os.write(head);
                }
                byte[] data = new byte[1024*1024*10];
                int len =0;
                while((len=is.read(data))!=-1){
                    os.write(data, 0, len);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
