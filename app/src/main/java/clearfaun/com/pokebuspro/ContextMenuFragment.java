package clearfaun.com.pokebuspro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by spencer on 3/2/2015.
 */
public class ContextMenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_maps, container, false);
        registerForContextMenu(root.findViewById(R.id.options_button));
        return root;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, R.id.a_item, Menu.NONE, "Menu zekee");
        menu.add(Menu.NONE, R.id.b_item, Menu.NONE, "Menu bobobo");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.a_item:
                Log.i("ContextMenu", "Item 1a was chosen");
                return true;
            case R.id.b_item:
                Log.i("ContextMenu", "Item 1b was chosen");
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
