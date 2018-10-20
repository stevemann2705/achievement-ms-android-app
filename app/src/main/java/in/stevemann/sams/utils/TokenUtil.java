package in.stevemann.sams.utils;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TokenUtil {
    private static String fileName = "session.token";
    public static void writeData(String data, Context context) {
        try {
            FileOutputStream fOut = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fOut.write(data.getBytes());
            fOut.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String readData(Context context) {

        String ret = "";

        try {
            FileInputStream fin = context.openFileInput(fileName);
            int c;
            String temp="";
            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
                ret = temp;
            } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

    public static boolean dataExists(Context context){
        boolean exists = false;
        try {
            InputStream inputStream = context.openFileInput(fileName);
            exists = true;
        } catch (FileNotFoundException e) {
            exists = false;
        }
        return exists;
    }

    public static void deleteData(Context context){
        context.deleteFile(fileName);
    }
}
