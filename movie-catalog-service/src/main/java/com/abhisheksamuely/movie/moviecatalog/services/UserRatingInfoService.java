package com.abhisheksamuely.movie.moviecatalog.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.abhisheksamuely.movie.moviecatalog.beans.Rating;
import com.abhisheksamuely.movie.moviecatalog.beans.UserRatingInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class UserRatingInfoService {
	
	@Autowired
	private WebClient.Builder builder;

	@HystrixCommand(fallbackMethod = "getFallbackUserRating",
			threadPoolKey = "userRatingInfoPool", threadPoolProperties = {
					@HystrixProperty(name = "coreSize", value = "20"), @HystrixProperty(name = "maxQueueSize", value = "10")
			})
	public UserRatingInfo getUserRating(String userId) {
		return builder.build().get().uri("http://ratings-data-service/ratings/users/" + userId).retrieve()
				.bodyToMono(UserRatingInfo.class).block();
	}
	
	public UserRatingInfo getFallbackUserRating(String userId) {
		UserRatingInfo rating = new UserRatingInfo();
		rating.setUserId(userId);
		rating.setRating(Arrays.asList(new Rating("0", 0)));
		return rating;
	}
}
