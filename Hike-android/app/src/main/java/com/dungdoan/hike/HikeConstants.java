package com.dungdoan.hike;

public class HikeConstants {
    public static final String TABLE_NAME = "hike_table";
    public static final String HIKE_ID = "hike_id";
    public static final String HIKE_NAME = "hike_name";
    public static final String HIKE_LOCATION = "hike_location";
    public static final String HIKE_DATE = "hike_date";
    public static final String HIKE_LENGTH = "hike_length";
    public static final String HIKE_PARKING = "hike_parking";
    public static final String HIKE_LEVEL = "hike_level";
    public static final String HIKE_DESCRIPTION = "hike_description";
    public static final String HIKE_IMAGE = "hike_image";
    public static final String HIKE_CREATED_AT = "hike_created_at";
    public static final String HIKE_UPDATED_AT = "hike_updated_at";
    public static final String CREATE_TABLE_QUERY = String.format(
                "CREATE TABLE %s (" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT)",
            TABLE_NAME,
            HIKE_ID,
            HIKE_NAME,
            HIKE_LOCATION,
            HIKE_DATE,
            HIKE_LENGTH,
            HIKE_PARKING,
            HIKE_LEVEL,
            HIKE_DESCRIPTION,
            HIKE_IMAGE,
            HIKE_CREATED_AT,
            HIKE_UPDATED_AT
    );
    public static final String DROP_TABLE_QUERY = String.format(
            "DROP TABLE IF EXISTS %s", TABLE_NAME
    );
}
