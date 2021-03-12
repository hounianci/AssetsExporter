package com.analyze;

import com.common.Lz4Util;

import java.io.File;
import java.io.FileInputStream;

public class TryLz4Decode {
    public static void main(String[] args) {
        File file = new File("lol/src/000448af5589769d5973e5d90cd2d23a.bytes");
        try(FileInputStream is = new FileInputStream(file)){
            is.skip(0x212);
            byte[] compressData = new byte[0x3cb-0x212];
            is.read(compressData);
            byte[] uncompressData = Lz4Util.decodeLz4(compressData);
            System.out.println(uncompressData.length);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
