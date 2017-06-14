package tk.apoorvmathur.multithreadtest;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;


public class MainActivity2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        String file = getIntent().getStringExtra("file");
        BitmapDrawable bitmapDrawable = null;
        GetImageDrawable getImageDrawable = new GetImageDrawable(bitmapDrawable, file, getBaseContext(), this);
        Thread thread = new Thread(getImageDrawable);
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void threadHandler(BitmapDrawable bitmapDrawable) {
        System.out.println("Handling Thread.");
        System.out.println(bitmapDrawable.getBounds());
        System.out.println(bitmapDrawable.setVisible(true, false));
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageDrawable(bitmapDrawable);
    }
}
