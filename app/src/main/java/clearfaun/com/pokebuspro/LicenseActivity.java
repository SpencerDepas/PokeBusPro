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
public class LicenseActivity extends Activity {

    String labels = "caption";
    String text = "";
    String[] s;
    private Vector<String> wordss;
    int j = 0;
    private StringTokenizer tokenizer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license_activity);

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.WHITE);

        wordss = new Vector<String>();

        TextView[] textViewArray = new TextView[3];
        textViewArray[0] = (TextView) findViewById(R.id.textViewOne);
        textViewArray[1] = (TextView) findViewById(R.id.textViewTwo);
        textViewArray[2] = (TextView) findViewById(R.id.textViewThree);
        readTxt(textViewArray);

    }


    private void readTxt(TextView[] textViewArray) {
        String names[] = new String[3];
        InputStream inputStream[] = new InputStream[3];
        inputStream[0] = getResources().openRawResource(R.raw.license_one);
        inputStream[1] = getResources().openRawResource(R.raw.licensetwo);
        inputStream[2] = getResources().openRawResource(R.raw.license_three);
        //     InputStream inputStream = getResources().openRawResource(R.raw.internals);
        for (int z = 0; z < inputStream.length; z ++){
                System.out.println(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int i;
            try {
                i = inputStream[z].read();
                while (i != -1) {
                    byteArrayOutputStream.write(i);
                    i = inputStream[z].read();
                }
                inputStream[z].close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            textViewArray[z].setText(byteArrayOutputStream.toString());

        }

    }

}
