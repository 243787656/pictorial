package com.bleyl.pictorial.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkUtil {

    private static final String IMGUR_DIRECT = "^(http|HTTP)(s|S)?://(www\\.)?(i\\.|m\\.)?imgur\\.com\\S+\\.(jpg|jpeg|gif|png|gifv|mp4|webm)$";
    private static final String IMGUR_ALBUM = "^(http|HTTP)(s|S)?://(www\\.)?(m\\.)?imgur\\.com/a/\\S+$";
    private static final String IMGUR_GALLERY = "^(http|HTTP)(s|S)?://(www\\.)?(m\\.)?imgur\\.com/gallery/\\S+$";
    private static final String IMGUR_SHORT = "^(http|HTTP)(s|S)?://(www\\.)?(i\\.|m\\.)?imgur\\.com\\S+$";
    private static final String GFYCAT = "^(http|HTTP)(s|S)?://(www\\.)?gfycat\\.com/\\S+$";
    private static final String DIRECT_GIF = "^(http|HTTP)(s|S)?://\\S+\\.gif$";

    private static final Pattern IMGUR_ID = Pattern.compile("(?<=\\.com/)\\w+");
    private static final Pattern IMGUR_GALLERY_ID = Pattern.compile("(?<=/gallery/)(?!=/)\\w+");
    private static final Pattern IMGUR_ALBUM_ID = Pattern.compile("(?<=/a/)(?!=/)\\w+");
    private static final Pattern GFYCAT_ID = Pattern.compile("(?<=\\.com/)\\w+");

    public enum LinkType {
        IMGUR_DIRECT,
        IMGUR_ALBUM,
        IMGUR_GALLERY,
        GFYCAT,
        DIRECT_GIF,
        DIRECT_IMAGE
    }

    public static LinkType getLinkType(String url) {
        if (url.matches(IMGUR_DIRECT)) {
            return LinkType.IMGUR_DIRECT;
        } else if (url.matches(IMGUR_ALBUM)) {
            return LinkType.IMGUR_ALBUM;
        } else if (url.matches(IMGUR_GALLERY)) {
            return LinkType.IMGUR_GALLERY;
        } else if (url.matches(IMGUR_SHORT)) {
            return LinkType.IMGUR_DIRECT;
        } else if (url.matches(GFYCAT)) {
            return LinkType.GFYCAT;
        } else if (url.matches(DIRECT_GIF)) {
            return LinkType.DIRECT_GIF;
        } else {
            return LinkType.DIRECT_IMAGE;
        }
    }

    public static String getImgurId(String url) {
        Matcher match = IMGUR_ID.matcher(url);
        if (match.find()) {
            return match.group(0);
        }
        return null;
    }

    public static String getImgurAlbumId(String url) {
        Matcher match = IMGUR_ALBUM_ID.matcher(url);
        if (match.find()) {
            return match.group(0);
        }
        return null;
    }

    public static String getImgurGalleryId(String url) {
        Matcher match = IMGUR_GALLERY_ID.matcher(url);
        if (match.find()) {
            return match.group(0);
        }
        return null;
    }

    public static String getGfycatCompatibleUrl(String url) {
        if (url.endsWith(".gifv")) {
            url = url.substring(0, url.length() - 1);
        }
        return url.replace("https", "http");
    }

    public static String getGfycatId(String url) {
        Matcher match = GFYCAT_ID.matcher(url);
        if (match.find()) {
            return match.group(0);
        }
        return null;
    }
}