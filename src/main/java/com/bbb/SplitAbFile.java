package com.bbb;

import com.common.ByteArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SplitAbFile {

    public static void main(String[] args) {
        File baseDir = new File("bbb/origin");
        byte[] header = new byte[]{0x55, 0x6e, 0x69, 0x74, 0x79, 0x46, 0x53  };
        for(File file : baseDir.listFiles()){
            int outputIdx = 0;
            int matchIdx = 0;
            String fileName = file.getName().substring(0, file.getName().lastIndexOf('.'));
            File outDir = new File("bbb/split/"+fileName);
            outDir.mkdir();
            try(FileInputStream is = new FileInputStream(file);){
                ByteArray outputData = new ByteArray();
                byte[] bb = new byte[1024*1024*10];
                int len = 0;
                while((len=is.read(bb))!=-1){
                    for(int i=0; i<len; i++){
                        outputData.addData(bb[i]);
                        if(bb[i]==header[matchIdx]){//匹配资源部头
                            matchIdx++;
                            if(matchIdx==header.length){//匹配完成 输出文件
                                matchIdx=0;
                                if(outputData.getDataLen()>20){
                                    File outputFile = new File("bbb/split/"+fileName+"/"+file.getName()+".p"+(outputIdx++));
                                    try(FileOutputStream os = new FileOutputStream(outputFile)){
                                        os.write(header);
                                        byte[] fileData = outputData.getData();
                                        os.write(fileData, 0, outputData.getDataLen()-header.length);
                                    }
                                }
                                outputData = new ByteArray();
                            }
                        }else {
                            matchIdx=0;
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
