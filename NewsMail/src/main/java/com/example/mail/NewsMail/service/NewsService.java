package com.example.mail.NewsMail.service;

import com.example.mail.NewsMail.model.Articles;
import com.example.mail.NewsMail.model.EmailContent;
import com.example.mail.NewsMail.model.NewsAPIResponse;
//import jakarta.ws.rs.client.Client;
//import jakarta.ws.rs.client.ClientBuilder;
//import jakarta.ws.rs.client.Invocation;
//import jakarta.ws.rs.client.WebTarget;
//import jakarta.ws.rs.core.MediaType;
//import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {

    private Logger LOGGER = LoggerFactory.getLogger(NewsService.class);

    public String getNews() throws URISyntaxException, IOException {

        RequestEntity entity = new RequestEntity(HttpMethod.GET, new URI("https://newsapi.org/v2/top-headlines?country=in&apiKey=apiKey"));
        RestTemplate template = new RestTemplate();
        ResponseEntity<NewsAPIResponse> response = template.exchange(entity, NewsAPIResponse.class);

//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(new URI("https://postman-echo.com/get"))
//                .GET()
//                .build();

//        URL url = new URL("https://newsapi.org/v2/top-headlines?country=in&apiKey=apiKey");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//        connection.connect();
//        System.out.println(connection.getResponseCode());
//        String body = connection.getResponseMessage();

//        Invocation.Builder builder;
//        WebTarget target;
//        Client client = ClientBuilder.newClient();
//        target = client.target("https://newsapi.org/v2/top-headlines?country=in&apiKey=apiKey");
//        builder = target.request(MediaType.APPLICATION_JSON);
//        Response response = builder.get();
//        NewsAPIResponse newsAPIResponse = response.readEntity(NewsAPIResponse.class);


        LOGGER.info("News retrieval successful");
        String body = populateContent(response.getBody());
        return body;
    }

    private String populateContent(NewsAPIResponse response){
        LOGGER.info("Retrieving content...");
        List<EmailContent> contentList = new ArrayList<>();
        for(Articles articles:response.getArticles()){
            EmailContent content = new EmailContent();
            content.setAuthor(articles.getAuthor());
            content.setTitle(articles.getTitle());
            content.setDescription(articles.getDescription());
            content.setUrl(articles.getUrl());
            contentList.add(content);
        }
        String emailBody = createTemplate(contentList);
        LOGGER.info("Content populated in template successfully");
        return emailBody;
    }

    private String createTemplate(List<EmailContent> contentList){
        LOGGER.info("Creating template...");
        final String AUTHOR = "Author", DESCRIPTION = "Description", TITLE = "Title", URL = "Url";
        String contentTemplate = "Top news for today: \n\n";
        LOGGER.info("Populating content...");
        for (EmailContent content: contentList){
            if(content.getAuthor() != null)
                contentTemplate += AUTHOR.concat(": \n"+content.getAuthor()+"\n");
            contentTemplate += TITLE.concat(": \n"+content.getTitle()+"\n");
//            if(content.getDescription() != null)
//                contentTemplate += DESCRIPTION.concat(": \n"+content.getDescription()+"\n");
            contentTemplate += URL.concat(": \n"+content.getUrl()+"\n");
            contentTemplate += "\n\n";
        }
        return contentTemplate;
    }

}
