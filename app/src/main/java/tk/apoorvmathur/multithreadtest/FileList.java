package tk.apoorvmathur.multithreadtest;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Apoorv on 5/2/2015.
 */
public class FileList implements Runnable {
    public static final int MODE_READ = 0, MODE_WRITE = 1;
    File file;
    ArrayList<String> fileList;
    int mode;
    MainActivity activity;

    public FileList(File file, int mode, MainActivity activity) {
        this.file = file;
        this.mode = mode;
        this.activity = activity;
        fileList = new ArrayList<String>();
    }

    public FileList(File file, int mode, ArrayList<String> fileList) {
        this.file = file;
        this.mode = mode;
        this.fileList = fileList;
    }

    @Override
    public void run() {
        task();
    }

    public synchronized void task() {
        if (mode == MODE_READ) {
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while (bufferedReader.ready()) {
                    String str = bufferedReader.readLine();
                    fileList.add(str);
                }
                bufferedReader.close();

                Handler handler = new Handler(Looper.getMainLooper()) {
                    public void handleMessage(Message inputMessage) {
                        FileList fileList = (FileList) inputMessage.obj;
                        activity.threadHandler(fileList.fileList);
                    }
                };
                handler.obtainMessage(1, this).sendToTarget();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mode == MODE_WRITE) {
            System.out.println("\n\n\n******* WRITING FILE ********");
            try {
                FileWriter fileWriter = new FileWriter(file);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                for (int i = 0; i < fileList.size(); i++) {
                    System.out.println("\n\n\n*******" + fileList.get(i) + "********");
                    printWriter.println(fileList.get(i));
                }
                printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
