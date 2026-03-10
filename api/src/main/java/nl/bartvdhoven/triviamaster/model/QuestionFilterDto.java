package nl.bartvdhoven.triviamaster.model;

/**
 * Data transfer object representing filter parameters used
 * to retrieve trivia questions.
 *
 * Supported filters include amount, category, difficulty and type.
 */
public class QuestionFilterDto {

	private Integer amount = 10;
	private Integer category;
	private String difficulty;
	private String type;
	
	public QuestionFilterDto() {};
	
	public QuestionFilterDto(int amount, Integer category, String difficulty, String type) {
		this.amount = amount;
		this.category = category;
		this.difficulty = difficulty;
		this.type = type;
	}

	@Override
	public String toString() {
		return "QuestionFilterDto [amount=" + amount + ", category=" + category + ", difficulty=" + difficulty
				+ ", type=" + type + "]";
	}

	public Integer getAmount() {
		return amount;
	}

	public Integer getCategory() {
		return category;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public String getType() {
		return type;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
