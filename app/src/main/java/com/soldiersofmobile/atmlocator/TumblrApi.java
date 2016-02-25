package com.soldiersofmobile.atmlocator;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface TumblrApi {


    @GET("/v2/blog/{name}.tumblr.com/posts")
    void getTumblrPosts(
            @Path("name") String blogName,
            @Query("api_key") String apiKey,
            @Query("limit") int limit,
            @Query("offset") int offset,
            Callback<TumblrResponse> callback);
}
