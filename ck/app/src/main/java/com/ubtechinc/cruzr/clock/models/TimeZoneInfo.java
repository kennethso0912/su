package com.ubtechinc.cruzr.clock.models;

import java.util.ArrayList;

/**
 * Created on 2017/5/23.
 *
 * @Author KennethSo
 * @Version 1.0.0
 * @Desc
 */
public class TimeZoneInfo {
   public ArrayList<TimeZone>  timezones=new ArrayList<TimeZone>();

    public static class TimeZone {
        String id;
        String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
