package nl.bartvdhoven.triviamaster.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Component;

import nl.bartvdhoven.triviamaster.model.AnswerDto;
import nl.bartvdhoven.triviamaster.model.Question;
import nl.bartvdhoven.triviamaster.model.ResultDto;

@Component
public class AnswerValidator {
	
	private Logger log = LoggerFactory.getLogger(AnswerValidator.class);
	
	private final QuestionMapper questionMapper;
	private final CacheService cacheService;
	
	public AnswerValidator(QuestionMapper questionMapper,
						   CacheService cacheService) {
		this.questionMapper = questionMapper;
		this.cacheService = cacheService;
	}
	
	/**
	 * Validates the answers submitted by a candidate during a quiz.
	 *
	 * For each submitted answer the corresponding question is retrieved from the
	 * cache. The submitted answer is then compared with the correct answer stored
	 * for that question.
	 *
	 * The result contains:
	 * - the question data
	 * - the submitted answer
	 * - the correct answer
	 * - whether the answer was correct
	 *
	 * @param answers list of answers submitted by the candidate
	 * @return list of validation results for each answered question
	 */
	public List<ResultDto> validateAnswers(List<AnswerDto> answers){
		
		List<ResultDto> results = new ArrayList<>();
		
		log.debug("Verifying {} answers", answers.size());
		
		for( AnswerDto answerDto : answers) {
			
			// retrieve question data from cache
			Question question = cacheService.get("questionsCache", answerDto.getQuestionId(), Question.class);
			
			// decode question HTML data
			String correctAnswer = StringEscapeUtils.unescapeHtml4(question.getCorrectAnswer());
			
			// create new result object with question data
			ResultDto result = new ResultDto(
					questionMapper.toDto(question),
					answerDto.getAnswer(),
					correctAnswer);
			
	        // Compare submitted answer with the correct answer
	        boolean isCorrect = Objects.equals(answerDto.getAnswer(), correctAnswer);
	        result.setResult(isCorrect);
			
			results.add(result);			
		}
		
		log.debug("Returned {} results", results.size());
		
		return results;
	}

}
