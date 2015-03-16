package clearfaun.com.pokebuspro;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by spencer on 3/15/2015.
 */
public class AboutScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_screen);

        View view = this.getWindow().getDecorView();

        view.setBackgroundColor(Color.WHITE);


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
