package client;


import model.BusStopExample;
import model.DistancesExample;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by SpencerDepas on 3/15/16.
 */
public interface GetBussStopInterface {


    @GET("/where/stops-for-location.json")
    void getBusStop(@Query("radius") String radius,
                    @Query("lat") String lat,
                    @Query("lon") String lng,
                    Callback<BusStopExample> response
    );

    @GET("/siri/stop-monitoring.json")
    void getBusDistancesFromStop(@Query("MonitoringRef") String buscode,
                                 @Query("MaximumStopVisits") String returnHowManyIncomingBuses,
                                 Callback<DistancesExample> response
    );


    @GET("/siri/stop-monitoring.json")
    DistancesExample getBusDistancesFromStopTwo(@Query("MonitoringRef") String busCode,
                                                @Query("MaximumStopVisits") String returnHowManyIncomingBuses
    );


}
