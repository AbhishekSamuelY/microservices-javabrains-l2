package com.abhisheksamuely.movie.moviecatalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.abhisheksamuely.movie.moviecatalog.beans.MovieCatalog;
import com.abhisheksamuely.movie.moviecatalog.beans.MovieInfo;
import com.abhisheksamuely.movie.moviecatalog.beans.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class MovieInfoService {

	@Autowired
	private RestTemplate template;

	/*
	 * commandProperties = {
	 * 
	 * @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",
	 * value = "2000"),
	 * 
	 * @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value = "5"),
	 * 
	 * @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value =
	 * "50"),
	 * 
	 * @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value =
	 * "5000") }
	 */

	@HystrixCommand(fallbackMethod = "getFallbackCatalogInfo", threadPoolKey = "movieInfoPool", threadPoolProperties = {
			@HystrixProperty(name = "coreSize", value = "20"), @HystrixProperty(name = "maxQueueSize", value = "10") })
	public MovieCatalog getCatalogInfo(Rating rating) {
		MovieInfo movie = template.getForObject("http://movie-info-service/movies/" + rating.getMovieId(),
				MovieInfo.class);
		return new MovieCatalog(movie.getName(), movie.getDescription(), rating.getRating());
	}

	public MovieCatalog getFallbackCatalogInfo(Rating rating) {
		MovieCatalog catalog = new MovieCatalog();
		catalog.setName("MOVIE NOT FOUND");
		catalog.setDescription("MOVIE DESCRIPTION NOT FOUND");
		catalog.setRating(0);
		return catalog;
	}
}
