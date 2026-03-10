package nl.bartvdhoven.triviamaster.model;

/**
 * Data transfer object representing the submitted answer.
 *
 * Contains the question Id and the answer submitted by the player.
 */
public class AnswerDto {
	
    private int questionId;
    private String answer;
    
	public AnswerDto(int questionId, String answer) {		
		this.questionId = questionId;
		this.answer = answer;
	}
	
	@Override
	public String toString() {
		return "AnswerDto [questionId=" + questionId + ", answer=" + answer + "]";
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
