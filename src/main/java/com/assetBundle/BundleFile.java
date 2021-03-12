package com.assetBundle;

import com.assetBundle.asset.AsObject;
import com.assetBundle.asset.Texture2D;
import com.assetBundle.serialized.ObjectInfo;
import com.assetBundle.serialized.SerializedFile;
import com.common.ArrayInputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BundleFile {

    private String fileName;
    private BundleHeader bundleHeader;
    private Map<String, ArrayInputStream> resourceFileReaders;
    private List<SerializedFile> assetsFileList;
    private List<AsObject> objects;
    private int stage = 0;
    private static final int HEAD=1, BLOCK=1<<1, NODE=1<<2;

    public BundleFile(ArrayInputStream is,String fileName)  {
        this.fileName = fileName;
        resourceFileReaders = new HashMap<>();
        assetsFileList = new ArrayList<>();
        objects = new ArrayList<>();
        try{
            bundleHeader = new BundleHeader(is);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        stage |= HEAD;
        try{
            bundleHeader.readBlockData(is);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        stage |= BLOCK;
        try{
            for(int i=0; i<bundleHeader.getNodeCount(); i++){
                BundleNodeInfo nodeInfo = bundleHeader.getNodeList().get(i);
                byte[] nodeData = nodeInfo.getNodeData();
                ArrayInputStream serializedIs = new ArrayInputStream(nodeData);
                if(SerializedFile.isSerializedFile(serializedIs)){
                    SerializedFile serializedFile = new SerializedFile(fileName, serializedIs);
                    assetsFileList.add(serializedFile);
                }else{
                    String s = nodeInfo.getPath();
                    resourceFileReaders.put(s, new ArrayInputStream(nodeInfo.getNodeData()));
                }
            }
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
                    if(obj!=null){
                        objects.add(obj);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        stage |= NODE;
    }

    public void outputFileDetail(File baseDir){
        File outputDir = new File(baseDir.getPath()+"/"+fileName);
        outputDir.mkdirs();
        String fileNamePrefix = baseDir.getPath()+"/"+fileName+"/"+fileName;
        StringBuilder infoBuilder = new StringBuilder(this.toString());
        //头文件信息
        try(FileOutputStream headerCompressOs=new FileOutputStream(new File(fileNamePrefix+".headerCom"));
            FileOutputStream headerUnCompressOs=new FileOutputStream(new File(fileNamePrefix+".headerUncom"));){
            headerCompressOs.write(bundleHeader.getCompressHeader());
            headerUnCompressOs.write(bundleHeader.getUncompressHeader());
        }catch (Exception e){
            e.printStackTrace();
        }
        //块数据
        infoBuilder.append("\n--- blockInfo ---\n");
        for(int i=0; i<bundleHeader.getBlockInfoCount(); i++){
            BundleBlockInfo blockInfo = bundleHeader.getBlockInfoList().get(i);
            infoBuilder.append("\n"+blockInfo.toString()+"\n");
            try(FileOutputStream blockOs = new FileOutputStream(new File(fileNamePrefix+".node_"+i+".blockComData"));
                FileOutputStream blockUncompressOs = new FileOutputStream(new File(fileNamePrefix+".node_"+i+".blockUncomData"));){
                blockOs.write(blockInfo.getCompressData());
                blockUncompressOs.write(blockInfo.getUncompressData());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //结点数据
        infoBuilder.append("\n--- nodeInfo ---\n");
        for(int i=0; i<bundleHeader.getNodeCount(); i++){
            BundleNodeInfo nodeInfo = bundleHeader.getNodeList().get(i);
            infoBuilder.append("\n"+nodeInfo.toString()+"\n");
            try(FileOutputStream nodeOs = new FileOutputStream(new File(fileNamePrefix+".node_"+i+".nodeData"));){
                nodeOs.write(nodeInfo.getNodeData());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //对象数据
        infoBuilder.append("\n--- objInfo ---\n");
        for(SerializedFile serializedFile : assetsFileList) {
            for (ObjectInfo objectInfo : serializedFile.getObjects()) {
                infoBuilder.append("\n"+objectInfo.toString()+"\n");
            }
        }
        //基础信息
        try(FileOutputStream infoOs = new FileOutputStream(new File(fileNamePrefix+".infoOut"));){
            infoOs.write(infoBuilder.toString().getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("stage:"+stage+"\n");
        builder.append("HeaderInfo:\n");
        builder.append(bundleHeader.toString()+"\n");
        builder.append("load Objects:\n");
        for(AsObject obj : objects){
            builder.append(obj.toString()+"\n");
        }
        return builder.toString();
    }
}
