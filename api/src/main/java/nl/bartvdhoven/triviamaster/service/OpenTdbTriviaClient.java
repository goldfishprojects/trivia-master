package nl.bartvdhoven.triviamaster.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import nl.bartvdhoven.triviamaster.model.OpenTdbResponse;
import nl.bartvdhoven.triviamaster.model.Question;
import nl.bartvdhoven.triviamaster.model.QuestionFilterDto;

/**
 * TriviaClient implementation that retrieves trivia questions from the
 * Open Trivia Database API (https://opentdb.com/api.php)
 * 
 * Uses Spring's {@link RestClient} to perform HTTP requests and
 * maps the API response to {@link Question} objects.
 */
@Component
public class OpenTdbTriviaClient implements TriviaClient{
	
	private static final Logger log = LoggerFactory.getLogger(OpenTdbTriviaClient.class);
	
	private final RestClient restClient;

	public OpenTdbTriviaClient(RestClient.Builder restClientBuilder) {
		this.restClient = restClientBuilder.baseUrl("https://opentdb.com/api.php")
										   .build();
	}

	/**
	 * Fetches trivia questions from OpenTDB based on the provided filter.
	 *
	 * A retry mechanism is used because the OpenTDB API may return
	 * HTTP 429 (Too Many Requests) when making multiple requests in a short period.
	 */
	@Override
	public List<Question> fetchQuestions(QuestionFilterDto filter) {
		
		log.debug("Fetching questions from OpenTDB with filter {}", filter.toString());

		// build uri based on provided filters
		String uri = OpenTdbUriBuilder.buildUri(filter);
		
		log.debug("Generated OpenTDB request URI: {}", uri);
		
		// fetch data using retry mechanism due to request limits of the OpenTdb API
		int maxAttempts = 3;

	    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
	        try {
	        	
	        	OpenTdbResponse response = restClient.get()
	                    .uri(uri)
	                    .retrieve()
	                    .body(OpenTdbResponse.class);
	        	
	            if (response == null) {
	            	log.warn("OpenTDB returned an empty response.");
	                throw new IllegalStateException("OpenTDB returned an empty response.");
	            }

	            log.info("Retrieved {} questions from OpenTDB", response.getQuestions().size());
	            return response.getQuestions();
	        	
	        } catch (RestClientResponseException ex) {

	        	// retry when statuscode = 429 (Too Many Requests)
	            if (ex.getStatusCode().value() == 429 && attempt < maxAttempts) {
	            	
	            	log.warn("OpenTDB rate limit hit, retrying attempt {}/{}", attempt, maxAttempts);
	            	
                     try {
                       // wait before retrying to avoid hitting the rate limit again
					   Thread.sleep(3000);
					 } catch (InterruptedException interruptedEx) {
	                   Thread.currentThread().interrupt();	                   
	                   log.warn("Thread was interrupted during retry delay: {}", interruptedEx);
	                   throw new IllegalStateException("Thread was interrupted during retry delay.", interruptedEx);
					 }
	            } else {
	            	log.error("Unexpected error while fetching questions from OpenTDB: {}", ex);
	                throw ex;
	            }
	        }	      	      
	    }	
	    
	    log.error("Failed to fetch questions from OpenTDB after {} attempts.", maxAttempts);
	    throw new IllegalStateException("Failed to fetch questions from OpenTDB after " + maxAttempts + " attempts.");
	}	

}
