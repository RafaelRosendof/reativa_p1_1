package com.reativaMVC1.mvc1.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reativaMVC1.mvc1.Aux.NewsCollector;
import com.reativaMVC1.mvc1.DAO.NewsDAO;
import com.reativaMVC1.mvc1.Entity.NewsEntity;
import com.reativaMVC1.mvc1.Redis.RedisImperativeService;

import java.util.List;

/*
 * Check-list
 * Create
 * Read
 * Update
 * Delete
 * Select by date and other fields
 */
@Service
public class NewsService {
 
    private final NewsDAO newsDAO;
    ;
    private final RedisImperativeService redisService;
    private final NewsCollector newsCollector = new NewsCollector();

    @Autowired
    public NewsService(NewsDAO newsDAO, RedisImperativeService redisService) {
        this.newsDAO = newsDAO;
        this.redisService = redisService;
    }

    public NewsEntity createNews(NewsEntity news) {
        return newsDAO.save(news);
    }

    public NewsEntity getNewsById(int id) {
        return newsDAO.findById(id).orElse(null);
    }
    public NewsEntity getNewsByTitle(String title) {
        return newsDAO.findByTitle(title);
    }

    public void deleteNews(int id) {
        newsDAO.deleteById(id);
    }
    public NewsEntity updateNews(int id, NewsEntity updatedNews) {
        return newsDAO.findById(id).map(news -> {
            news.setFont(updatedNews.getFont());
            news.setTitle(updatedNews.getTitle());
            news.setDescription(updatedNews.getDescription());
            news.setUrl(updatedNews.getUrl());
            news.setStock_id(updatedNews.getStock_id());
            return newsDAO.save(news);
        }).orElse(null);
    }

    public List<NewsEntity> getAllNews() {
        return newsDAO.findAll();
    }

    public NewsEntity getNewsByUrl(String url) {
        return newsDAO.findByUrl(url);
    }

    // Other methods for the redis service

    public String createNews(String stock){

        // função que vai retornar a notícia já pronta tudo feito ja  
        return newsCollector.processNewsData(stock);
    }

    public String getNewsTop1(){

        String top1 = redisService.getTop2Request().get(0);

        String news1 = createNews(top1);

        System.out.println("News 1: " + news1);
        
        return news1;

    }

    public String getNewsTop2(){

        String top2 = redisService.getTop2Request().get(1);

        String news2 = createNews(top2);

        System.out.println("News 1: " + news2);
        
        return news2;
    
    }

    public String giveBackPrompt(){
        
        return "This is a prompt";
    }

}

/*
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.5.6</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.ms1Service</groupId>
	<artifactId>ms1</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>ms1Service</name>
	<description>parte do ms1 para rotear os serviços para o ms2 e ms3 AI</description>
	<url/>
	<licenses>
		<license/>
	</licenses>
	<developers>
		<developer/>
	</developers>
	<scm>
		<connection/>
		<developerConnection/>
		<tag/>
		<url/>
	</scm>
	<properties>
		<java.version>21</java.version>
		<spring-cloud.version>2025.0.0</spring-cloud.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jdbc</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-graphql</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.graphql</groupId>
			<artifactId>spring-graphql-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>${spring-cloud.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>
 */