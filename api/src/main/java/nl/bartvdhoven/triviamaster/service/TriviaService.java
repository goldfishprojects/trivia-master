package nl.bartvdhoven.triviamaster.service;

import java.util.List;

import nl.bartvdhoven.triviamaster.model.AnswerDto;
import nl.bartvdhoven.triviamaster.model.QuestionDto;
import nl.bartvdhoven.triviamaster.model.QuestionFilterDto;
import nl.bartvdhoven.triviamaster.model.ResultDto;

/**
 * Service responsible for trivia game logic.
 *
 * Provides operations for retrieving trivia questions and validating
 * answers submitted by a player.
 */
public interface TriviaService {
	
    /**
     * Retrieves trivia questions based on provided filter.
     *
     * @param filter filters used to select questions (amount, category, difficulty...)
     * @return list of trivia questions
     */
	List<QuestionDto> getAllQuestions(QuestionFilterDto filter);
	
    /**
     * Verifies trivia answers submitted by a player.
     *
     * Each submitted answer is compared with the correct answer of the
     * corresponding question and a result is returned.
     *
     * @param answers list of answers submitted by the player
     * @return validation results for each answered question
     */
	List<ResultDto> validateAnswers(List<AnswerDto> answers);
}
