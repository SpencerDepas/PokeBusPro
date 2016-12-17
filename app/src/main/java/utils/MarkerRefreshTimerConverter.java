package utils;

/**
 * Created by SpencerDepas on 12/11/16.
 */

public class MarkerRefreshTimerConverter {

    public int convertStringToInt(final String findWhatToPreSelect){
        int preSelectedIndex = 0;
        try {
            switch (Integer.parseInt(findWhatToPreSelect)) {
                case 20:
                    preSelectedIndex = 0;
                    break;
                case 30:
                    preSelectedIndex = 1;
                    break;
                case 60:
                    preSelectedIndex = 2;
                    break;
                default:
                    preSelectedIndex = 3;
                    break;

            }
        } catch (Exception e) {
            preSelectedIndex = 3;
        }
        return preSelectedIndex;
    }
}
