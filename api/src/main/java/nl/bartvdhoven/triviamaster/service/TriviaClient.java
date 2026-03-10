package nl.bartvdhoven.triviamaster.service;

import java.util.List;

import nl.bartvdhoven.triviamaster.model.Question;
import nl.bartvdhoven.triviamaster.model.QuestionFilterDto;

/**
 * Client used to retrieve trivia questions from an external source.
 */
public interface TriviaClient {
	
    /**
     * Fetches trivia questions based on the provided filter criteria.
     *
     * @param filter filter parameters such as category, difficulty and amount
     * @return list of retrieved questions
     */
    List<Question> fetchQuestions(QuestionFilterDto filter);
}
