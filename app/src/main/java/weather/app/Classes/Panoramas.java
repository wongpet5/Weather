package weather.app.Classes;

import java.util.ArrayList;
import java.util.List;

public class Panoramas {

    public int count ;
    public boolean hasMore;
    public PanoramasMapLocation panoromasMapLocation;
    public List<PanoramasPhoto> panomarasPhotos;

    public Panoramas()
    {
        count = 0;
        hasMore = true;
        panoromasMapLocation = new PanoramasMapLocation();
        panomarasPhotos = new ArrayList<PanoramasPhoto>();
    }

}
