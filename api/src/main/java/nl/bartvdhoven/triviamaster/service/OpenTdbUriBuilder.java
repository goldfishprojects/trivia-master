package nl.bartvdhoven.triviamaster.service;

import org.springframework.web.util.UriComponentsBuilder;

import nl.bartvdhoven.triviamaster.model.QuestionFilterDto;

/**
 * Utility class responsible for building request URIs for the
 * Open Trivia Database API.
 *
 * Converts the filter parameters provided in {@link QuestionFilterDto}
 * into query parameters that can be appended to the OpenTDB API endpoint.
 */
public class OpenTdbUriBuilder {
	
	// private constructor to prevent instantiation of utility class
	private OpenTdbUriBuilder() {}
	
    /**
     * Builds a URI query string based on the provided filter parameters.
     *
     * Only parameters that are present in the filter will be included
     * in the generated query string.
     *
     * @param filter filter containing query parameters such as amount,
     *               category, difficulty and question type
     * @return URI query string that can be used for the OpenTDB API request
     */
	public static String buildUri(QuestionFilterDto filter) {
		
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("");

        builder.queryParam("amount", filter.getAmount());

        if (filter.getCategory() != null) {
            builder.queryParam("category", filter.getCategory());
        }
        if (filter.getDifficulty() != null) {
            builder.queryParam("difficulty", filter.getDifficulty());
        }
        if (filter.getType() != null) {
            builder.queryParam("type", filter.getType());
        }   
                
        return builder.toUriString(); 
    }

}
