package com.tdj;

import com.assetBundle.BundleFile;
import com.common.ArrayInputStream;

import java.io.File;
import java.io.FileInputStream;

public class ReadTdjAsFile {
    public static void main(String[] args) {
        File dir = new File("tdj/header/");
        for(File file : dir.listFiles()){
            if(file.getName().startsWith("_")){
                continue;
            }
            String fileName = file.getName();
            String outputPath = "tdj/headerOut/";
            File outputDir = new File(outputPath);
            outputDir.mkdirs();
            TdjBundleFile bundleFile = null;
            try(FileInputStream is = new FileInputStream(file);){
                ArrayInputStream ais = new ArrayInputStream(is);
                bundleFile = new TdjBundleFile(ais, file.getName());
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                bundleFile.outputFileDetail(new File("tdj/headerOut/"));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
