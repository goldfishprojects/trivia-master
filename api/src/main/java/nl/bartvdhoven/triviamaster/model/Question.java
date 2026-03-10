package nl.bartvdhoven.triviamaster.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a trivia question returned by the Open Trivia Database API.
 *
 * Each question is assigned a locally generated unique id so that
 * submitted answers can later be validated using cached question data.
 * 
 * {@link JsonProperty} is used to map JSON fields from the API response
 * to the corresponding Java fields.
 */
public class Question {
	
	// thread safe increment for id
	private static final AtomicInteger idCount = new AtomicInteger(0); 
	
	private final int id;
	private final String type;
	private final String difficulty;
	private final String category;
	private final String question;
	
	@JsonProperty("correct_answer")
	private final String correctAnswer;
	
	@JsonProperty("incorrect_answers")
	private final List<String> incorrectAnswers;
	
	public Question(String type, String difficulty, String category, String question,
			@JsonProperty("correct_answer")	String correctAnswer,
			@JsonProperty("incorrect_answers") List<String> incorrectAnswers) {
		this.id = idCount.incrementAndGet();
		this.type = type;
		this.difficulty = difficulty;
		this.category = category;
		this.question = question;
		this.correctAnswer = correctAnswer;
		this.incorrectAnswers = Collections.unmodifiableList(incorrectAnswers != null ? new ArrayList<>(incorrectAnswers) : new ArrayList<>());
	}	

	@Override
	public String toString() {
		return "Question [id=" + id + ", type=" + type + ", difficulty=" + difficulty + ", category=" + category
				+ ", question=" + question + ", correctAnswer=" + correctAnswer + ", incorrectAnswers="
				+ incorrectAnswers + "]";
	}

	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public String getCategory() {
		return category;
	}

	public String getQuestion() {
		return question;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public List<String> getIncorrectAnswers() {
	    return incorrectAnswers;
	}
	
}
