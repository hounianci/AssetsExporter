package com.bbb;

import com.common.ByteArray;
import com.common.FileByteUtils;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4SafeDecompressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ReplaceHeader {
    public static void main(String[] args) {
        ByteArray header = readHeader();
        File splitDir = new File("bbb/replaceSrc");
        StringBuilder errorFile = new StringBuilder();
        int successSize = 0;
        int failSize = 0;
        for(File splitSubDir : splitDir.listFiles()){
            File outputDir = new File("bbb/output/"+splitSubDir.getName());
            outputDir.mkdir();
            for(File file : splitSubDir.listFiles()){
                byte[] bb = new byte[1024*1024*10];
                int len = 0;
                try(FileInputStream is = new FileInputStream(file)){
                    is.skip(0x32);
                    ByteArray zipData = new ByteArray();
                    byte[] zipB = new byte[1];
                    byte[][] zipEnd = {{(byte) 0xf0}, {0x1,0x0}};
                    int[] zipMatchIdx = new int[zipEnd.length];
                    int zipEndMatchIdx = 0;
                    a:while(is.read(zipB)!=-1){
                        boolean zipEndMatch = false;
                        for(int i=0; i<zipEnd[zipEndMatchIdx].length; i++){
                            if(zipB[0]==zipEnd[zipEndMatchIdx][i]){
                                zipEndMatch = true;
                                zipMatchIdx[zipEndMatchIdx] = i;
                                break ;
                            }
                        }
                        if(zipEndMatch){
                            zipEndMatchIdx++;
                            if(zipEndMatchIdx==zipEnd.length){
                                break a;
                            }
                        }else{
                            if(zipEndMatchIdx==1){
                                zipData.addData(zipEnd[0][zipMatchIdx[0]]);
                            }
                            zipEndMatchIdx = 0;
                            zipData.addData(zipB[0]);
                        }
                    }
                    if(zipData.getDataLen()>300){
                        continue;
                    }
                    header.setLong(0x1e, file.length(), true);
                    header.setInt(0x26, zipData.getDataLen(), true);
                    header.setInt(0x2a, calcUncompressDataSize(zipData), true);
                    try(FileOutputStream os = new FileOutputStream(new File("bbb/output/"+splitSubDir.getName()+"/"+file.getName()+".output"))){
                        os.write(header.getData());
                        os.write(zipData.getData());
                        os.write(zipEnd[0][zipMatchIdx[0]]);
                        os.write(zipEnd[1][zipMatchIdx[1]]);
                        while ((len=is.read(bb))!=-1){
                            os.write(bb, 0, len);
                        }
                    }
                    successSize++;
                }catch (Exception e){
                    errorFile.append(file.getName()+"\n");
                    System.out.println(file.getName());
                    System.out.println(e.getMessage());
                    System.out.println("----");
                    failSize++;
                }
            }
        }
        try(FileOutputStream os = new FileOutputStream(new File("bbb/output/error.txt"))){
            os.write(("success:"+successSize+"\n").getBytes());
            os.write(("fail:"+failSize+"\n").getBytes());
            os.write(errorFile.toString().getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int calcUncompressDataSize(ByteArray compressData){
        LZ4Factory factory = LZ4Factory.fastestInstance();
        LZ4SafeDecompressor decompressor = factory.safeDecompressor();
        byte[] uncompressData = new byte[0xff];
        decompressor.decompress(compressData.getData(), 0, compressData.getDataLen(), uncompressData, 0);
        int idx = 0xff-1;
        //从后往前删0，只保留最后一个
        while(uncompressData[idx] == 0){
            idx--;
        }
        return idx+2;
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
