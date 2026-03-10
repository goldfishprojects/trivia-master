package nl.bartvdhoven.triviamaster.model;

/**
 * Data transfer object representing the result of a submitted answer.
 *
 * Contains the question, the submitted answer, the correct answer
 * and whether the submitted answer was correct.
 */
public class ResultDto {
	
    private QuestionDto question;
    private String submittedAnswer;
    private String correctAnswer;
    private Boolean result;
    
	public ResultDto(QuestionDto question,
					 String submittedAnswer,
					 String correctAnswer) {		
		this.question = question;
		this.submittedAnswer = submittedAnswer;
		this.correctAnswer = correctAnswer;	
	}

	public QuestionDto getQuestion() {
		return question;
	}

	public void setQuestion(QuestionDto question) {
		this.question = question;
	}

	public String getSubmittedAnswer() {
		return submittedAnswer;
	}

	public void setSubmittedAnswer(String submittedAnswer) {
		this.submittedAnswer = submittedAnswer;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

}
