package nl.bartvdhoven.triviamaster.service;

import java.util.List;

import org.springframework.stereotype.Service;

import nl.bartvdhoven.triviamaster.model.AnswerDto;
import nl.bartvdhoven.triviamaster.model.Question;
import nl.bartvdhoven.triviamaster.model.QuestionDto;
import nl.bartvdhoven.triviamaster.model.QuestionFilterDto;
import nl.bartvdhoven.triviamaster.model.ResultDto;

/**
 * Default implementation of {@link TriviaService}.
 *
 * Responsible for the retrieval of trivia questions and the validation
 * of submitted answers by delegating work to the appropriate
 * components like {@link TriviaClient}, {@link QuestionMapper}
 * and {@link AnswerValidator}.
 */
@Service
public class DefaultTriviaService implements TriviaService {
		
	private final TriviaClient triviaClient;
	private final QuestionMapper mapper;
	private final AnswerValidator answerValidator;
	private final CacheService cacheService;
	
	public DefaultTriviaService(TriviaClient triviaClient,
							 QuestionMapper mapper,
							 AnswerValidator answerValidator,
							 CacheService cacheService){
		this.triviaClient = triviaClient;
		this.mapper = mapper;
		this.answerValidator = answerValidator;
		this.cacheService = cacheService;
	}	


	@Override
	public List<QuestionDto> getAllQuestions(QuestionFilterDto filter) {				
        List<Question> questions = triviaClient.fetchQuestions(filter);
        
        // Store original question data in cache so submitted answers
        // can later be validated using the question id.
        questions.forEach(question -> cacheService.put("questionsCache", question.getId(), question));

        return mapper.toDtoList(questions);
	}
	
	@Override
	public List<ResultDto> validateAnswers(List<AnswerDto> answers) {		
		return answerValidator.validateAnswers(answers);		
	}
	
		
}



