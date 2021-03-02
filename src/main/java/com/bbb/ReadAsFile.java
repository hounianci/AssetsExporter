package com.bbb;

import com.assetBundle.BundleBlockInfo;
import com.assetBundle.BundleHeader;
import com.assetBundle.BundleNodeInfo;
import com.assetBundle.ObjectReader;
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

    public static Map<String, ArrayInputStream> resourceFileReaders;
    public static List<SerializedFile> assetsFileList;

    public static void main(String[] args) {
        File dir = new File("bbb/header/");
        for(File file : dir.listFiles()){
            resourceFileReaders = new HashMap<>();
            assetsFileList = new ArrayList<>();
            if(file.getName().startsWith("_")){
                continue;
            }
            String fileName = file.getName();
            String outputPath = "bbb/headerOut/"+fileName;
            File outputDir = new File(outputPath);
            outputDir.mkdirs();
            try(FileInputStream is = new FileInputStream(file);
                FileOutputStream infoOs = new FileOutputStream(new File(outputPath+"/"+fileName+".infoOut"));
                FileOutputStream headerUncompressOs = new FileOutputStream(new File(outputPath+"/"+fileName+".headerOut"));){
                BundleHeader header = new BundleHeader(is);
                headerUncompressOs.write(header.getUncompressHeader());
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
                    StringBuilder nodeInfoBuilder = new StringBuilder();
                    BundleNodeInfo nodeInfo = header.getNodeList().get(i);
                    byte[] nodeData = nodeInfo.getNodeData();
                    try(FileOutputStream nodeOs = new FileOutputStream(new File(outputPath+"/"+nodeInfo.getPath()+".node"))){
                        nodeOs.write(nodeData);
                    }
                    nodeInfoBuilder.append(nodeInfo.toString()+"\n");
                    ArrayInputStream serializedIs = new ArrayInputStream(nodeData);
                    if(SerializedFile.isSerializedFile(serializedIs)){
                        SerializedFile serializedFile = new SerializedFile(fileName, serializedIs);
                        assetsFileList.add(serializedFile);
                        for(ObjectInfo objectInfo : serializedFile.getObjects()){
                            nodeInfoBuilder.append(objectInfo.toString()+"\n");
                        }
                    }else{
                        String s = nodeInfo.getPath();
                        resourceFileReaders.put(s, new ArrayInputStream(nodeInfo.getNodeData()));
                    }
                    infoOs.write(nodeInfoBuilder.toString().getBytes());
                }
                infoOs.write("\n--- node end ---\n".getBytes());
                infoOs.write("\n--- object start ---\n".getBytes());
                for(SerializedFile serializedFile : assetsFileList){
                    for(ObjectInfo objectInfo : serializedFile.getObjects()){
                        ArrayInputStream inputStream = new ArrayInputStream(serializedFile.getData());
                        ObjectReader objectReader = new ObjectReader(inputStream, serializedFile, objectInfo);
                        AsObject obj = null;
                        switch (objectInfo.getClassID()){
                            case 28:
                                obj = new Texture2D(objectReader);
                                break;
                        }
                        if(obj != null){
                            infoOs.write(("\n"+obj.toString()+"\n").getBytes());
                        }
                    }
                }
                infoOs.write("\n--- object end ---\n".getBytes());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
