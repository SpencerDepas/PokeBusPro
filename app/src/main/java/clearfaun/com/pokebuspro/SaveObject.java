package clearfaun.com.pokebuspro;

import android.util.Log;

import com.google.android.gms.plus.model.people.Person;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by spencer on 3/30/2015.
 */
public class SaveObject {


    public void saveObject(BusInfo businfo){
        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("/sdcard/save_object).bin"))); //Select where you wish to save the file...
            oos.writeObject(businfo); // write the class as an 'object'
            oos.flush(); // flush the stream to insure all of the information was written to 'save_object.bin'
            oos.close();// close the stream
        }
        catch(Exception ex){
            Log.i("SaveObject", ex.getMessage());
            ex.printStackTrace();
        }
    }


    public Object loadSerializedObject(File f){
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            Object o = ois.readObject();
            return o;
        }
        catch(Exception ex){
            Log.v("SaveObject",ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

}
