package model;

import java.util.ArrayList;
import java.util.List;
 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class Departures {

     @SerializedName("all")
     @Expose
     private List<All> all = new ArrayList<All>();

     /**
      *
      * @return
      * The all
      */
     public List<All> getAll() {
         return all;
     }

     /**
      *
      * @param all
      * The all
      */
     public void setAll(List<All> all) {
         this.all = all;
     }

 }