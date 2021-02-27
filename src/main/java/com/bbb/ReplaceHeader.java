package com.bbb;

import com.common.ByteArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ReplaceHeader {
    public static void main(String[] args) {
        ByteArray header = readHeader();
        File splitDir = new File("bbb/split");
        for(File splitSubDir : splitDir.listFiles()){
            File outputDir = new File("bbb/output/"+splitSubDir.getName());
            outputDir.mkdir();
            for(File file : splitSubDir.listFiles()){
                byte[] bb = new byte[1024*1024*10];
                int len = 0;
                try(FileInputStream is = new FileInputStream(file)){
                    try(FileOutputStream os = new FileOutputStream(new File("bbb/output/"+splitSubDir.getName()+"/"+file.getName()+".output"))){
                        is.skip(0x32);
                        ByteArray zipData = new ByteArray();
                        byte[] zipB = new byte[1];
                        byte[] zipEnd = {(byte) 0xf0, 0x1};
                        int zipEndMatchIdx = 0;
                        while(is.read(zipB)!=-1){
                            if(zipB[0]==zipEnd[zipEndMatchIdx]){
                                zipEndMatchIdx++;
                                if(zipEndMatchIdx==zipEnd.length){
                                    break;
                                }
                            }else{
                                zipEndMatchIdx = 0;
                                zipData.addData(zipB[0]);
                            }
                        }
                        if(zipData.getDataLen()>300){
                            continue;
                        }
                        header.setLong(0x1e, file.length());
                        header.setInt(0x26, zipData.getDataLen());
                        header.setInt(0x2a, 0xff);
                        os.write(header.getData());
                        os.write(zipData.getData());
                        os.write(zipEnd);
                        while ((len=is.read(bb))!=-1){
                            os.write(bb, 0, len);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static ByteArray readHeader(){
        File headerFile = new File("bbb/format/resSHeader.txt");
        ByteArray headerData = new ByteArray();
        byte[] headerB = new byte[1024];
        int len = 0;
        try(FileInputStream is = new FileInputStream(headerFile)){
            while((len=is.read(headerB))!=-1){
                headerData.addData(headerB, 0, len);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return headerData;
    }

}
