package com.abhisheksamuely.movie.moviecatalog.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abhisheksamuely.movie.moviecatalog.beans.MovieCatalog;
import com.abhisheksamuely.movie.moviecatalog.beans.UserRatingInfo;
import com.abhisheksamuely.movie.moviecatalog.services.MovieInfoService;
import com.abhisheksamuely.movie.moviecatalog.services.UserRatingInfoService;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {

	@Autowired
	private DiscoveryClient client;
	
	@Autowired
	MovieInfoService movieService;
	
	@Autowired
	UserRatingInfoService userRatingService;

	@GetMapping("/{userId}")
	public List<MovieCatalog> getCatalog(@PathVariable("userId") String userId) {

		/*
		 * TODO:movie info microservice 1. get list of rated movies 2. for each movie
		 * id, call movie info service and get details 3. put them all together
		 * 
		 * TODO: datastore 1. when user makes data check if data is present in the data
		 * store 1. if present fetch it 2. if not make not api call store it into
		 * database then return it 3. start of service update data store based id(movie
		 * id)
		 */

		client.getInstances(userId);

		// using webClient.Builder
		UserRatingInfo ratings = userRatingService.getUserRating(userId);

		return ratings.getRating().stream().map(rating -> movieService.getCatalogInfo(rating)).collect(Collectors.toList());

	}

	

	
}
