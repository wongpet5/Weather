package weather.app.Classes;

import java.util.Date;

public class PanoramasPhoto {

    public Date upload_date;

    public int owner_id;

    public double width;
    public double height;
    public double latitude;
    public double longitude;

    public String photo_file_url;
    public String photo_id;
    public String photo_title;
    public String photo_url;
    public String owner_name;
    public String owner_url;

    public PanoramasPhoto()
    {
        upload_date = new Date();
        owner_id = 0;
        width = 0;
        height = 0;
        latitude = 0;
        longitude = 0;
        photo_file_url = "";
        photo_id = "";
        photo_title = "";
        photo_url = "";
        owner_name = "";
        owner_url = "";
    }

}
