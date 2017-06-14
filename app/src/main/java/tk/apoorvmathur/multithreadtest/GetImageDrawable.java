package tk.apoorvmathur.multithreadtest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Apoorv on 5/5/2015.
 */
public class GetImageDrawable implements Runnable {

    private MainActivity2Activity  activity;
    String file;
    Context context;
    BitmapDrawable bitmapDrawable;

    public GetImageDrawable(BitmapDrawable bitmapDrawable, String file, Context context, MainActivity2Activity activity) {
        this.activity = activity;
        this.file = file;
        this.context = context;
        this.bitmapDrawable = bitmapDrawable;
    }
    public void run() {
        File imageFile = new File(context.getFilesDir(), this.file);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap image = BitmapFactory.decodeFile(imageFile.getPath());
        double h = image.getHeight();
        double w = image.getWidth();
        System.out.println(w+" "+h);
        int width =600;
        double scaling = 1;
        if(w>width) {
            scaling = width/w;
        }
        h = (h*scaling);
        w = (w*scaling);
        int imageHeight = (int) Math.abs(h);
        int imageWidth = (int) Math.abs(w);
        options.outHeight = imageHeight;
        options.outWidth = imageWidth;
        System.out.println(imageWidth+" "+imageHeight+" "+scaling);
        image = BitmapFactory.decodeFile(imageFile.getPath());
        image = ThumbnailUtils.extractThumbnail(image, imageWidth, imageHeight);
        System.out.println(image.getWidth()+" "+image.getHeight());
        bitmapDrawable = new BitmapDrawable(Resources.getSystem(), image);
        Handler handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message inputMessage) {
                GetImageDrawable imageLoader = (GetImageDrawable) inputMessage.obj;
                activity.threadHandler(imageLoader.bitmapDrawable);
            }
        };
        handler.obtainMessage(1, this).sendToTarget();
    }
}
