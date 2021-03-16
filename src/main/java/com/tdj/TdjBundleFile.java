package com.tdj;

import com.assetBundle.BundleFile;
import com.assetBundle.BundleHeader;
import com.common.ArrayInputStream;

import java.io.FileOutputStream;

public class TdjBundleFile extends BundleFile {
    public TdjBundleFile(ArrayInputStream is, String fileName) {
        super(is, fileName);
    }
    @Override
    protected BundleHeader createHeader() {
        return new TdjBundleHeader();
    }

    @Override
    protected void outputOtherData(String fileNamePrefix) {
        try(FileOutputStream os = new FileOutputStream(fileNamePrefix+".unknownData")){
            TdjBundleHeader header = (TdjBundleHeader) getBundleHeader();
            os.write(header.getHeadUnknownData());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
