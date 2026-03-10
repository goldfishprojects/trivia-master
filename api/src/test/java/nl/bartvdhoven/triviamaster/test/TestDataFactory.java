package nl.bartvdhoven.triviamaster.test;

import java.util.Arrays;

import nl.bartvdhoven.triviamaster.model.Question;
import nl.bartvdhoven.triviamaster.model.QuestionDto;

/**
 * Utility class used to generate reusable test data for unit tests.
 *
 * Provides factory methods for creating {@link Question} and {@link QuestionDto}
 * objects with predefined values so tests remain consistent.
 */
public class TestDataFactory {

    public static Question createDefaultQuestion() {
        return new Question(
                "multiple",
                "easy",
                "General Knowledge",
                "What is the answer?",
                "Correct",
                Arrays.asList("Wrong1", "Wrong2", "Wrong3")
        );
    }
    
    public static Question createQuestionWithHtml() {
        return new Question(
                "multiple",
                "easy",
                "Entertainment: Cartoon &amp; Animations",
                "What&#039;s the Team Fortress 2 Scout&#039;s city of origin?",
                "&quot;Super Smash Bros. for Nintendo 3DS&quot;",
                Arrays.asList(
                        "Assume temperature is 25&deg;C.",
                        "Computer Personal Unit",
                        "Central Processor Utility"
                )
        );
    }
    
    public static QuestionDto createDefaultQuestionDto(int questionId) {
        return new QuestionDto(
        		questionId,
                "multiple",
                "easy",
                "General Knowledge",
                "What is the answer?",
                Arrays.asList("Correct", "Wrong1", "Wrong2", "Wrong3")
        );
    }
    
    public static QuestionDto createQuestionDtoWithHtml(int questionId) {
        return new QuestionDto(
        		questionId,
                "multiple",
                "easy",
                "Entertainment: Cartoon & Animations",
                "What's the Team Fortress 2 Scout's city of origin?",
                Arrays.asList("\"Super Smash Bros. for Nintendo 3DS\"",
                			  "Assume temperature is 25°C.", 
                			  "Computer Personal Unit", 
                			  "Central Processor Utility")
        );
    }
    
}
