package client;


import model.BusStopExample;
import model.DistancesExample;
import model.UkBusStopsLocation;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
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
                    Callback<UkBusStopsLocation> response
    );

    @GET("/siri/stop-monitoring.json")
    void getBusDistancesFromStop(@Query("atcocode") String atCode,
                                 @Query("app_id") String appId,
                                 @Query("app_key") String appKey,
                                 Callback<DistancesExample> response
    );

    @GET("/siri/stop-monitoring.json")
    void fgetBusDistancesFromStop(@Query("MonitoringRef") String buscode,
                                 @Query("MaximumStopVisits") String returnHowManyIncomingBuses,
                                 Callback<DistancesExample> response
    );


    @GET("/siri/stop-monitoring.json")
    DistancesExample getBusDistancesFromStopTwo(@Query("MonitoringRef") String busCode,
                                                @Query("MaximumStopVisits") String returnHowManyIncomingBuses
    );


}
