package com.bbb;

import com.assetBundle.BundleBlockInfo;
import com.assetBundle.BundleHeader;
import com.assetBundle.BundleNodeInfo;
import com.assetBundle.serialized.SerializedFile;
import com.common.ArrayInputStream;
import com.common.ByteArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ReadAsFile {

    public static Map<String, ArrayInputStream> resourceFileReaders;

    public static void main(String[] args) {
        resourceFileReaders = new HashMap<>();
        File dir = new File("bbb/header/");
        for(File file : dir.listFiles()){
            if(file.getName().startsWith("_")){
                continue;
            }
            String fileName = file.getName().substring(0, file.getName().indexOf('.'));
            String outputPath = "bbb/headerOut/"+fileName;
            File outputDir = new File(outputPath);
            outputDir.mkdirs();
            try(FileInputStream is = new FileInputStream(file);
                FileOutputStream infoOs = new FileOutputStream(new File(outputPath+"/"+fileName+".intoOut"));
                FileOutputStream headerUncompressOs = new FileOutputStream(new File(outputPath+"/"+fileName+".headerOut"));){
                BundleHeader header = new BundleHeader(is);
                infoOs.write(header.toString().getBytes());
                infoOs.write("\n--- block start ---\n".getBytes());
                for(int i=0; i<header.getBlockInfoCount(); i++){
                    BundleBlockInfo block = header.getBlockInfoList().get(i);
                    infoOs.write(block.toString().getBytes());
                    try(FileOutputStream blockOs = new FileOutputStream(new File(outputPath+"/"+fileName+"_block-"+i+".blockOut"))){
                        blockOs.write(block.getUncompressData());
                    }
                }
                infoOs.write("\n--- block end ---\n".getBytes());
                infoOs.write("\n--- node start ---\n".getBytes());
                for(int i=0; i<header.getNodeCount(); i++){
                    BundleNodeInfo nodeInfo = header.getNodeList().get(i);
                    byte[] nodeData = nodeInfo.getNodeData();
                    try(FileOutputStream nodeOs = new FileOutputStream(new File(outputPath+"/"+fileName+"_node-"+i+".nodeOut"))){
                        nodeOs.write(nodeData);
                    }
                    infoOs.write((nodeInfo.toString()+"\n").getBytes());
                    ArrayInputStream serializedIs = new ArrayInputStream(nodeData);
                    if(SerializedFile.isSerializedFile(serializedIs)){
                        SerializedFile serializedFile = new SerializedFile(fileName, serializedIs);
                    }else{
                        String s = nodeInfo.getPath();
                        resourceFileReaders.put(s, new ArrayInputStream(nodeInfo.getNodeData()));
                    }
                }
                infoOs.write("\n--- node end ---\n".getBytes());
                headerUncompressOs.write(header.getUncompressHeader());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
