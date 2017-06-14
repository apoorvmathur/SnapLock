package tk.apoorvmathur.multithreadtest;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends Activity {

    ArrayList<String> fileList;
    private TableRow lastRow;
    String Key;

    public static void threadHandler(Button button, BitmapDrawable bitmapDrawable, File thumb) {
        button.setBackground(bitmapDrawable);
        final String thumbs = thumb.getName();
        System.out.println(thumbs);
        button.setClickable(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), thumbs, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), MainActivity2Activity.class);
                intent.putExtra("file", thumbs);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("\n\n\n*******" + savedInstanceState + "********");
        super.onCreate(savedInstanceState);
        Key = getIntent().getStringExtra("password");
        setContentView(R.layout.activity_main);
        fileList = new ArrayList<String>();
        inflateTable();
        if (savedInstanceState == null) {
            System.out.println("\n\n\n*******" + "READING FILE" + "********");
            FileList fileList = new FileList(new File(getFilesDir(), "files.txt"), FileList.MODE_READ, this);
            new Thread(fileList).start();
        } else {
            ArrayList<String> fileListRes = (ArrayList<String>) savedInstanceState.getSerializable("fileList");
            for (int i = 0; i < fileListRes.size(); i++) {
                System.out.println("\n\n\n LOOPING " + i);
                addFile(new File(getFilesDir(), fileListRes.get(i)));
            }
        }
    }

    private void addFile(File file) {
        Button button = new Button(this);
        ImageLoader imageLoader = new ImageLoader(button, file);
        new Thread(imageLoader).start();
        addButton(button);
        fileList.add(file.getName());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("fileList", fileList);
        super.onSaveInstanceState(outState);
    }

    private void inflateTable() {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        tableLayout.removeAllViewsInLayout();
        TableRow tableRow = new TableRow(this);
        lastRow = tableRow;
        tableLayout.addView(tableRow);
    }

    private void addButton(Button button) {
        System.out.println("\n\n\n ADDING BUTTON");
        if (lastRow.getChildCount() == 3) {
            TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
            TableRow tableRow = new TableRow(this);
            lastRow = tableRow;
            tableLayout.addView(tableRow);
        }
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
        layoutParams.setMargins(2,2,2,2);
        button.setLayoutParams(layoutParams);
        lastRow.addView(button);
        System.gc();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_add) {
            int CODE = 0;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            Uri FPath = data.getData();
            addUri(FPath);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void addUri(Uri FPath) {
        Button button = new Button(this);
        ContentResolver contentResolver = getContentResolver();
        int filename = 0;
        if (fileList.size() > 0) {
            filename = Integer.parseInt(fileList.get(fileList.size() - 1)) + 1;
        }
        File file = new File(getFilesDir(), filename + "_file");
        File thumbnail = new File(getFilesDir(), filename + "");
        fileList.add(filename + "");
        ImageLoader imageLoader = new ImageLoader(button, FPath, file, thumbnail, contentResolver, Key);
        new Thread(imageLoader).start();
        addButton(button);
        FileList fileList = new FileList(new File(getFilesDir(), "files.txt"), FileList.MODE_WRITE, this.fileList);
        new Thread(fileList).start();
        System.gc();
    }

    public void threadHandler(ArrayList<String> fileList) {
        this.fileList = fileList;
        recreate();
        System.gc();
    }
}
