package nl.bartvdhoven.triviamaster.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object used to store the response returned by the OpenTdb API.
 *
 * Contains the response code and a list of {@link Question} objects.
 * 
 * {@link JsonProperty} is used to map JSON fields from the API response
 * to the corresponding Java fields.
 */
public class OpenTdbResponse {

	 @JsonProperty("response_code")
	 private final String responseCode;
	 
	 @JsonProperty("results")
	 private final List<Question> questions;
	 
	 public OpenTdbResponse(
			 @JsonProperty("response_code") String responseCode, 
			 @JsonProperty("results")List<Question> questions
	 ) {
		this.responseCode = responseCode;
		this.questions = Collections.unmodifiableList(questions != null ? new ArrayList<>(questions) : new ArrayList<>());
	 }	 

	 @Override
	 public String toString() {
		 return "OpenTdbResponse [responseCode=" + responseCode + ", questions=" + questions + "]";
	 }

	 public String getResponseCode() {
		 return responseCode;
	 }

	 public List<Question> getQuestions() {
		 return questions;
	 }
	
}
