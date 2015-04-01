package clearfaun.com.pokebuspro;



/**
 * Created by spencer on 4/1/2015.
 */
public class MyCustomObject {
    private MapsActivity mContext;

    void MyCustomObject(MapsActivity context) {
        this.mContext = context;
    }

    void showDialog(String message) {
        com.gc.materialdesign.widgets.Dialog dialog = new com.gc.materialdesign.widgets.Dialog(mContext, "Set your PokeBus", message);
        dialog.show();
    }
}