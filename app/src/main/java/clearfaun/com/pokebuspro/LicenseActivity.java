package clearfaun.com.pokebuspro;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by spencer on 4/5/2015.
 */
public class LicenseActivity extends Activity{

    String labels="caption";
    String text="";
    String[] s;
    private Vector<String> wordss;
    int j=0;
    private StringTokenizer tokenizer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license_activity);

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.WHITE);

        wordss = new Vector<String>();
        TextView helloTxt = (TextView)findViewById(R.id.textView);
        helloTxt.setText(readTxt());


    }


    private String readTxt(){

        InputStream inputStream = getResources().openRawResource(R.raw.license_one);
            //     InputStream inputStream = getResources().openRawResource(R.raw.internals);
        System.out.println(inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        try {
            i = inputStream.read();
            while (i != -1)
            {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return byteArrayOutputStream.toString();
    }



}
