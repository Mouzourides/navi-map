package com.mouzourides.navi_map.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GooglePlace implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -4041502421563593320L;
    //@Key
    private String name;
    //@Key
    private String vicinity;
    //@Key
    private List<String> types;
    //@Key
    private String scope;

    public static class Geometry implements Serializable {
        /**
         *
         */
        private static final long serialVersionUID = 2946649576104623502L;

        public static class Location implements Serializable {
            /**
             *
             */
            private static final long serialVersionUID = -1861462299276634548L;
            //@Key
            private double lat;
            //@Key
            private double lng;

            /**
             * @return the lat
             */
            public double getLat() {
                return lat;
            }

            /**
             * @param lat the lat to set
             */
            public void setLat(double lat) {
                this.lat = lat;
            }

            /**
             * @return the lng
             */
            public double getLng() {
                return lng;
            }

            /**
             * @param lng the lng to set
             */
            public void setLng(double lng) {
                this.lng = lng;
            }
        }

        //@Key
        private Location location;

        /**
         * @param location the location to set
         */
        public void setLocation(Location location) {
            this.location = location;
        }

        /**
         * @return the location
         */
        public Location getLocation() {
            return location;
        }
    }

    //@Key
    private Geometry geometry;

    public static class Opening_Hours implements Serializable{
        private static final long serialVersionUID = 6L;
        //@Key
        private List exceptional_date;
        //@Key
        private boolean open_now;
        //@Key
        private List weekday_text;

        public List getExceptionalDate() {
            return exceptional_date;
        }

        public void setExceptionalDate(List exceptional_date) {
            this.exceptional_date = exceptional_date;
        }

        public boolean getOpenNow() {
            return open_now;
        }

        public void setOpenNow(boolean open_now) {
            this.open_now = open_now;
        }

        public List getWeekdayText() {
            return weekday_text;
        }

        public void setWeekDayText(List weekday_text){
            this.weekday_text = weekday_text;
        }
    }

    //@Key
    private Opening_Hours opening_hours;

    public static class Photos implements Serializable {
        private static final long serialVersionUID = -4041502441513593320L;
        //@Key
        private int height;
        //@Key
        private String[] html_attributions;
        //@Key
        private String photo_reference;
        //@Key
        private int width;

        /**
         * @return the height
         */
        public int getHeight() {
            return height;
        }

        /**
         * @param height the height to set
         */
        public void setHeight(int height) {
            this.height = height;
        }

        /**
         * @return the html_attributions
         */
        public String[] getHtmlAttributions() {
            return html_attributions;
        }

        /**
         * @param html_attributions the html_attributions to set
         */
        public void setHtmlAttributions(String[] html_attributions) {
            this.html_attributions = html_attributions;
        }

        /**
         * @param photo_reference the photo_reference to set
         */
        public void setPhotoReference(String photo_reference) {
            this.photo_reference = photo_reference;
        }

        /**
         * @return the photo_reference
         */
        public String getPhotoReference() {
            return photo_reference;
        }

        /**
         * @return the width
         */
        public int getWidth() {
            return width;
        }

        /**
         * @param width the width to set
         */
        public void setWidth(int width) {
            this.width = width;
        }
    }

    //@Key
    private Photos[] photos;

    //@Key
    private String icon;

    //@Key
    private String id;

    //@Key
    private String reference;

    //@Key
    private float rating;

    //@Key
    private String url;

    public GooglePlace() {
        types = new ArrayList<String>();
    }

    /**
     * @param geometry the geometry to set
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    /**
     * @return the geometry
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * @param opening_hours the opening_hours to set
     */
    public void setOpeningHours(Opening_Hours opening_hours){
        this.opening_hours = opening_hours;
    }

    /**
     * @return the opening_hours
     */
    public Opening_Hours getOpeningHours(){
        return opening_hours;
    }

    /**
     * @param photos the photos to set
     */
    public void setPhotos(Photos photos[]){
        this.photos = photos;    }

    /**
     * @return the photos
     */
    public Photos getPhotos() {
        return photos[0];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public List<String> getTypes() {
        return types;
    }

    /**
     * @param types the types to set
     */
    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void addType(String type) {
        types.add(type);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }


}