package clearfaun.com.pokebuspro;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by spencer on 3/16/2015.
 */
public class BusMap extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bus_map);


        /*int k = 2;
        int width = 2102;
        int height = 2214;


        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                ImageView iv = (ImageView)findViewById(R.id.bus_map);
                InputStream istream =   null;
                try {
                    istream = this.getContentResolver().openInputStream(Uri.fromFile(getFileStreamPath(this.getFilesDir() + File.separator + "C:\\Users\\spencer\\AndroidStudioProjects\\PokeBusPro\\app\\src\\main\\res\\drawable\\busbkln_map.png")));
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                BitmapRegionDecoder decoder     =   null;
                try {
                    decoder = BitmapRegionDecoder.newInstance(istream, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int nw = (j*width/k);
                int nh = (i*height/k);

                Bitmap bMap = decoder.decodeRegion(new Rect(nw,nh, (nw+width/k),(nh+height/k)), null);
                iv.setImageBitmap(bMap);

            }
        }*/




    }
}
