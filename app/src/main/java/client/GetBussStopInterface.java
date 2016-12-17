package client;


import model.BusStopDistances;
import model.DistancesExample;
import model.UkBusStopsLocation;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by SpencerDepas on 3/15/16.
 */
public interface GetBussStopInterface {

    @GET("/stops/near.json")
    void getBusStop(@Query("lat") String lat,
                    @Query("lon") String lng,
                    @Query("app_id") String appId,
                    @Query("app_key") String appKey,
                    @Query("rpp") String numberOfReturnedBustops,
                    Callback<UkBusStopsLocation> response);

    @GET("/stop/{atcocode}/live.json")
    void getBusDistancesFromStop(@Path("atcocode") String atCode,
                                 @Query("app_id") String appId,
                                 @Query("app_key") String appKey,
                                 @Query("group") String group,
                                 @Query("nextbuses") String nextbuses,
                                 @Query("limit") String limit,
                                 Callback<BusStopDistances> response);

    @GET("/siri/stop-monitoring.json")
    DistancesExample getBusDistancesFromStopTwo(@Query("MonitoringRef") String busCode,
                                                @Query("MaximumStopVisits") String returnHowManyIncomingBuses);
}
