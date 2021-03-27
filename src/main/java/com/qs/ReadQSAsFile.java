package com.qs;

import com.assetBundle.BundleFile;
import com.common.ArrayInputStream;
import com.common.ByteArray;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4SafeDecompressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ReadQSAsFile {
    public static void main(String[] args) {
        File dir = new File("qs/header/");
        processDir(dir);
    }

    public static void processDir(File dir){
        for(File file : dir.listFiles()){
            if(file.isDirectory()){
                processDir(file);
            }else{
                processFile(file);
            }
        }
    }

    public static void processFile(File file){
        if(file.getName().startsWith("_")){
            return;
        }
        String fileName = file.getName();
        String outputPath = "qs/headerOut/";
        File outputDir = new File(outputPath);
        outputDir.mkdirs();
        BundleFile bundleFile = null;
        try(FileInputStream is = new FileInputStream(file);){
            ArrayInputStream ais = new ArrayInputStream(is);
            bundleFile = new BundleFile(ais, file.getName());
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            bundleFile.outputFileDetail(new File("qs/headerOut/"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
