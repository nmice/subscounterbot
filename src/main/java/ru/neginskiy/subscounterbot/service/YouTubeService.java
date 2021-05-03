package ru.neginskiy.subscounterbot.service;


import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YouTubeService {

    public final String youTubeApiKey;

    public YouTubeService(@Value("${social.youTubeApiKey}") String youTubeApiKey) {
        this.youTubeApiKey = youTubeApiKey;
    }

    public String getYouTubeSubsCount(String youTube) {
        YouTube youtube = new YouTube.Builder(
                new NetHttpTransport(),
                new JacksonFactory(),
                request -> {
                })
                .setApplicationName("youtube-cmdline-search-sample")
                .setYouTubeRequestInitializer(new YouTubeRequestInitializer(youTubeApiKey))
                .build();
        try {
            YouTube.Search.List search = youtube.search().list("snippet");
            search.setQ(youTube);
            search.setType("channel");
            List<SearchResult> searchResultList = search.execute().getItems();
            SearchResult searchResult = searchResultList.get(0);
            String channelId = searchResult.getSnippet().getChannelId();
            YouTube.Channels.List channels = youtube.channels().list("snippet, statistics");
            channels.setId(channelId);
            ChannelListResponse channelResponse = channels.execute();
            Channel c = channelResponse.getItems().get(0);
            return c.getStatistics().getSubscriberCount().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Неизвестно";
    }
}
