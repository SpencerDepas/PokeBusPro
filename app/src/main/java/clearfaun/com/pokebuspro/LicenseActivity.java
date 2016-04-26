package clearfaun.com.pokebuspro;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 * Created by spencer on 4/5/2015.
 */
public class LicenseActivity extends AppCompatActivity {

    String labels = "caption";
    String text = "";
    String[] s;
    private Vector<String> wordss;
    int j = 0;
    private StringTokenizer tokenizer;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license_activity_main);

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.WHITE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

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
