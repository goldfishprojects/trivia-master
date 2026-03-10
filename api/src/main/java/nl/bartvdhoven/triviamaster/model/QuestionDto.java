package nl.bartvdhoven.triviamaster.model;

import java.util.Collections;
import java.util.List;

/**
 * Data transfer object representing a trivia question that is sent to the client.
 *
 * Contains the decoded question text and a shuffled list of possible answers.
 * The correct answer is included in the answers list but is not labeled
 * to prevent the client from knowing the correct answer beforehand.
 */
public class QuestionDto {
	
    private final int id;
    private final String type;
    private final String difficulty;
    private final String category;
    private final String question;
    private final List<String> answers;
    
	public QuestionDto(int id, String type, String difficulty, String category, String question, List<String> answers) {
		this.id = id;
		this.type = type;
		this.difficulty = difficulty;
		this.category = category;
		this.question = question;
		this.answers = Collections.unmodifiableList(answers);
	}
	
	@Override
	public String toString() {
		return "QuestionDto [id=" + id + ", type=" + type + ", difficulty=" + difficulty + ", category=" + category
				+ ", question=" + question + ", answers=" + answers + "]";
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

	public List<String> getAnswers() {
		return answers;
	} 
    
}

