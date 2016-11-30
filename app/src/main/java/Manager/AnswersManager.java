package Manager;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

import model.AnswersConstants;

/**
 * Created by SpencerDepas on 11/30/16.
 */
public class AnswersManager {


    private static AnswersManager sAnswersManager = new AnswersManager();

    public static AnswersManager getInstance() {
        if (sAnswersManager == null) {
            sAnswersManager = new AnswersManager();
        }
        return sAnswersManager;
    }

    public void deleteFavoriteBusStops() {

        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Deleted favorite Buses")
                .putContentType(AnswersConstants.ACTION));
    }


    public void refreshDialog() {
        Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName("Refresh Timer Dialog")
                        .putContentType(AnswersConstants.ACTION)
        );
    }

    public void enableLocation() {

        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Enable Location")
                .putContentType(AnswersConstants.ACTION)
                .putCustomAttribute("Fine permission enabled", "true")
                .putCustomAttribute("Enable GPS intent fired", "true"));
    }

    public void fineLocationDenied() {

        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Enable Location")
                .putContentType(AnswersConstants.ACTION)
                .putCustomAttribute("Fine permission enabled", "False"));
    }

    public void mapOnPress() {
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(AnswersConstants.MAP_ON_PRESS)
                .putContentType(AnswersConstants.SELECTION));
    }

    public void selectMap(String boroughs) {
        Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName("Select Map")
                        .putContentType("Selection")
                        .putCustomAttribute(AnswersConstants.MAP_SELECTION, boroughs)
        );
    }

    public void followMeOnTwitter() {
        Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName("Follow me on twitter")
                        .putContentType(AnswersConstants.ACTION)

        );
    }

    public void fabOnRefreshPressed() {
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(AnswersConstants.FAB_ON_REFRESH)
                .putContentType(AnswersConstants.ACTION));
    }

    public void setRadius(String radius) {
        Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName(AnswersConstants.SET_RADIUS)
                        .putContentType(AnswersConstants.ACTION)
                        .putCustomAttribute(AnswersConstants.RADIUS, radius)

        );
    }

    public void setRefreshTime(String time) {
        Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName(AnswersConstants.REFRESH_TIMER_TIME)
                        .putContentType(AnswersConstants.SELECTION)
                        .putCustomAttribute(AnswersConstants.TIME, time)
        );
    }

    public void setFavBusStopDialog() {
        Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName(AnswersConstants.SET_FAV_BUS_STOP_DIALOG)
                        .putContentType(AnswersConstants.ACTION)
        );
    }


    public void selectedFavBus(String buscode) {
        Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName(AnswersConstants.SET_FAV_BUS_STOP_DIALOG)
                        .putContentType(AnswersConstants.SELECTION)
                        .putCustomAttribute(AnswersConstants.FAV_BUS_STOP, buscode)
        );

    }

    public void fineLocationPermissionAsked(String buscode) {
        Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName(AnswersConstants.SET_FAV_BUS_STOP_DIALOG)
                        .putContentType(AnswersConstants.SELECTION)
                        .putCustomAttribute(AnswersConstants.FAV_BUS_STOP, buscode)
        );

    }

    public void pressedMyLocation() {
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(AnswersConstants.PRESSED_MY_LOCATION)
                .putContentType(AnswersConstants.ACTION));
    }




    public void openNavView() {
        Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName("Nav view open")
                        .putContentType(AnswersConstants.ACTION)
        );
    }


    public void launchMapActivty() {
        Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName(AnswersConstants.LAUNCH_MAP_ACTIVITY)
                        .putContentType(AnswersConstants.ACTION)
        );
    }


    public void searchedForBus(String address) {
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(AnswersConstants.SEARCHED_FOR_BUS)
                .putContentType(AnswersConstants.SELECTION)
                .putCustomAttribute(AnswersConstants.ADDRESS_SEARCHED, address));
    }

}
