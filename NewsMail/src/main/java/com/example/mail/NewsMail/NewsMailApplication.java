package com.example.mail.NewsMail;

import com.example.mail.NewsMail.service.EmailSenderService;
import com.example.mail.NewsMail.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.JobExecutionExitCodeGenerator;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;

@SpringBootApplication
public class NewsMailApplication {

	@Autowired
	EmailSenderService service;
	@Autowired
	NewsService newsService;

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(NewsMailApplication.class, args);
		ExitCodeGenerator generator = new JobExecutionExitCodeGenerator();
		SpringApplication.exit(context, generator);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void sendEmail() throws URISyntaxException, IOException {
		String content = newsService.getNews();
		System.out.println(content);
		service.sendMail("recipientEmail", "News for "+ LocalDate.now(), content.toString());
	}

}
