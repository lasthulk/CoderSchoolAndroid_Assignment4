package com.tam.advancedtwitter.models;

import org.json.JSONArray;
import org.json.JSONObject;

public class Media {
    private String thumbnailImage;
    private String videoUrl;
    private String mediaType;

    public String getGetMediaType() {
        return mediaType;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public Media() {
        this.thumbnailImage = "";
        this.videoUrl = "";
        this.mediaType = "";
    }

    public static Media getMedia(JSONObject extendedEntities) {
        Media media = new Media();
        try {
            if (extendedEntities != null) {
                JSONArray mediaArray = extendedEntities.optJSONArray("media");
                if (mediaArray != null && mediaArray.length() > 0) {
                    JSONObject mediaObject = mediaArray.optJSONObject(0);
                    String mediaUrl = mediaObject.getString("media_url");
                    if (mediaUrl != null && !mediaUrl.isEmpty()) {
                        media.thumbnailImage = mediaUrl;
                    }
//                    else {
//                        Log.d("media_url", "No media_url ");
//                    }
                    JSONObject videoObject = mediaObject.optJSONObject("video_info");
                    if (videoObject != null) {
                        JSONArray variants = videoObject.optJSONArray("variants");
                        if (variants != null && variants.length() > 0) {
                            for (int i = 0; i < variants.length(); i++) {
                                JSONObject curVariant = variants.getJSONObject(i);
                                if (curVariant.getString("content_type").equals("video/mp4")) {
                                    media.videoUrl = curVariant.getString("url");
//                                    Log.d("video_url", "video_url: " + media.videoUrl);
                                    break;
                                }
                            }
                        }
                    }
                    media.mediaType = mediaObject.optString("type");
                }
            }
            return media;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return media;
    }
}