package com.dungdoan.hike;

public class ObservationConstants {
    public static final String TABLE_NAME = "observation_table";
    public static final String OBSERVATION_ID = "observation_id";
    public static final String OBSERVATION_NAME = "observation_name";
    public static final String OBSERVATION_TIME = "observation_time";
    public static final String OBSERVATION_COMMENT = "observation_comment";
    public static final String OBSERVATION_IMAGE = "observation_image";
    public static final String OBSERVATION_CREATED_AT = "observation_created_at";
    public static final String OBSERVATION_UPDATED_AT = "observation_updated_at";
    public static final String OBSERVATION_HIKE_ID = "observation_hike_id";
    public static final String CREATE_TABLE_QUERY = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT)",
            TABLE_NAME,
            OBSERVATION_ID,
            OBSERVATION_NAME,
            OBSERVATION_TIME,
            OBSERVATION_COMMENT,
            OBSERVATION_IMAGE,
            OBSERVATION_CREATED_AT,
            OBSERVATION_UPDATED_AT,
            OBSERVATION_HIKE_ID
    );
    public static final String DROP_TABLE_QUERY = String.format(
            "DROP TABLE IF EXISTS %s", TABLE_NAME
    );
}
