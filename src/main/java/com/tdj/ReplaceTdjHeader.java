package com.tdj;

import java.io.File;
import java.io.FileInputStream;

public class ReplaceTdjHeader {
    public static void main(String[] args) {
        File dir = new File("tdj/header/_ui_loginui_Origin.b");
        try(FileInputStream is = new FileInputStream(dir)){
            is.skip(0x32);
            byte[] data = new byte[70];
            is.read(data);
            for(int i=0; i<data.length; i++){
                int x = 0xcd;
                int y = 0xff&data[i];
                System.out.print(Integer.toHexString(x^y)+" ");
                if((i+1)%10==0){
                    System.out.println();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
