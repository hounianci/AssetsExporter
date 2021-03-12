package com.bbb;

import com.assetBundle.*;
import com.assetBundle.asset.AsObject;
import com.assetBundle.asset.NamedAsObject;
import com.assetBundle.asset.Texture2D;
import com.assetBundle.serialized.ObjectInfo;
import com.assetBundle.serialized.SerializedFile;
import com.common.ArrayInputStream;
import com.common.ByteArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadAsFile {

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
            BundleFile bundleFile = null;
            try(FileInputStream is = new FileInputStream(file);){
                ArrayInputStream ais = new ArrayInputStream(is);
                bundleFile = new BundleFile(ais, file.getName());
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
