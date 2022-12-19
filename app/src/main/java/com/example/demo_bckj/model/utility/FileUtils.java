package com.example.demo_bckj.model.utility;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.SystemClock;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author ZJL
 * @date 2022/12/19 13:43
 * @des
 * @updateAuthor
 * @updateDes
 */
public  class FileUtils {

    static String imgPath= Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + "bc" + File.separator + "img";
    /**
     * 将bitmap保存到内存卡中
     * @param bitmap
     */
    public static void saveBitmapToSdcard(Bitmap bitmap){

        File file = new File(imgPath+ SystemClock.currentThreadTimeMillis()+".jpg");
        file.setWritable(true);//设置执行权限
        file.setReadable(true);//设置读权限
        file.setExecutable(true);//设施写权限
        if(!file.exists()){//如果目录不存在就创建目录
            file.mkdir();
        }
        FileOutputStream out = null;
        try{
            out = new FileOutputStream(file);
            if(bitmap != null){
                /*
                 * 三个参数的含义分别是：
                 * 1.保存图片的格式
                 * 2.标识图片质量0~100.质量越小压缩的越小（这里设置100标识不压缩）。另外如果图片是png格式，压缩是无损的，将忽略此参数（设置无效）
                 * 3.向OutputStream写入图片数据
                 */
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(out != null){
                    out.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
