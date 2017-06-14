package tk.apoorvmathur.multithreadtest;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.NoSuchPaddingException;

/**
 * Created by Apoorv on 5/1/2015.
 */
public class ImageLoader implements Runnable {
    public Button button;
    public BitmapDrawable bitmapDrawable;
    Uri uri;
    File file;
    File thumbnail;
    ContentResolver contentResolver;
    private int mode;
    private String Key;

    public ImageLoader(Button button, Uri uri, File file, File thumbnail, ContentResolver contentResolver, String key) {
        this.button = button;
        this.uri = uri;
        this.file = file;
        this.contentResolver = contentResolver;
        this.thumbnail = thumbnail;
        this.Key = key;
        mode = 1;
    }

    public ImageLoader(Button button, File file) {
        this.button = button;
        this.thumbnail = file;
        mode = 0;
    }

    @Override
    public void run() {
        System.gc();
        task();
    }

    private synchronized void task() {
        try {
            if (mode == 1) {
                importPic();
                generateThumbs();
            }
            readThumbs();
            Handler handler = new Handler(Looper.getMainLooper()) {
                public void handleMessage(Message inputMessage) {
                    ImageLoader imageLoader = (ImageLoader) inputMessage.obj;
                    if(file == null) {
                        file = new File(thumbnail.getParentFile(), thumbnail.getName()+"_file");
                    }
                    MainActivity.threadHandler(imageLoader.button, imageLoader.bitmapDrawable, file);
                }
            };
            handler.obtainMessage(1, this).sendToTarget();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void importPic() throws IOException {
        InputStream imageStream = null;
        imageStream = contentResolver.openInputStream(uri);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] buf = new byte[1024];
        int len;
        while ((len = imageStream.read(buf)) > 0) {
            fileOutputStream.write(buf, 0, len);
        }
        imageStream.close();
        fileOutputStream.close();
    }

    private void generateThumbs() throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        FileOutputStream fileOutputStream = new FileOutputStream(thumbnail);
        Bitmap image = BitmapFactory.decodeFile(file.getPath(), options);
        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(image, 185, 185);
        bitmapDrawable = new BitmapDrawable(Resources.getSystem(), ThumbImage);
        ThumbImage.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        fileOutputStream.close();
    }

    private void readThumbs() {
        Bitmap image = BitmapFactory.decodeFile(thumbnail.getPath());
        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(image, 185, 185);
        bitmapDrawable = new BitmapDrawable(Resources.getSystem(), ThumbImage);
    }

    private void encryptPic() throws IOException, InvalidKeySpecException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        FileInputStream fileInputStream = new FileInputStream(file);
        File outputFile = new File(file.getParentFile(), file.getName()+"Encrypted");
        outputFile.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        CryptoProvider.encrypt(fileInputStream, fileOutputStream, Key);
    }

    private void decryptPic() throws IOException, InvalidKeySpecException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        FileInputStream fileInputStream = new FileInputStream(file);
        File outputFile = new File(file.getParentFile(), file.getName()+"Encrypted");
        outputFile.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        CryptoProvider.decrypt(fileInputStream, fileOutputStream, Key);
    }
}
