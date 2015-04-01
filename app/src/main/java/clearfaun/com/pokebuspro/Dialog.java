package clearfaun.com.pokebuspro;

import android.app.AlertDialog;

/**
 * Created by spencer on 4/1/2015.
 */
class DialogObject {
    private MapsActivity mContext;

    void DialogObject(MapsActivity context) {
        this.mContext = context;
    }

    private void showDialog(String message) {

        com.gc.materialdesign.widgets.Dialog dialog = new com.gc.materialdesign.widgets.Dialog(mContext, "Set your PokeBus", message);
        dialog.show();


    }
}