package com.abhisheksamuely.movie.movieinfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.abhisheksamuely.movie.movieinfo.beans.MovieInfo;
import com.abhisheksamuely.movie.movieinfo.pojo.MovieSummary;

@RestController
@RequestMapping("/movies")
public class MovieInfoController {

	@Value("${api.key}")
	private String apiKey;

	@Autowired
	private RestTemplate template;

	@GetMapping("/{movieId}")
	public MovieInfo getMovieInfo(@PathVariable("movieId") String movieId) {
		MovieSummary summary = template.getForObject(
				"https://api.themoviedb.org/3/movie/"+movieId+"?api_key="+apiKey,
				MovieSummary.class);
		System.out.println(summary.getTitle());
		return new MovieInfo("1", "fast and furious", "action movie");
	}
}
