package nl.bartvdhoven.triviamaster.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;

import nl.bartvdhoven.triviamaster.model.AnswerDto;
import nl.bartvdhoven.triviamaster.model.Question;
import nl.bartvdhoven.triviamaster.model.QuestionDto;
import nl.bartvdhoven.triviamaster.model.QuestionFilterDto;
import nl.bartvdhoven.triviamaster.model.ResultDto;

import static nl.bartvdhoven.triviamaster.test.TestDataFactory.createDefaultQuestion;
import static nl.bartvdhoven.triviamaster.test.TestDataFactory.createDefaultQuestionDto;
import static nl.bartvdhoven.triviamaster.test.TestDataFactory.createQuestionWithHtml;

class DefaultTriviaServiceTest {
	
	private DefaultTriviaService defaultTriviaService;
	private OpenTdbTriviaClient mockTriviaClient;
	private QuestionMapper mockQuestionMapper;
	private AnswerValidator mockAnswerValidator;
	private CacheService mockCacheService;
	
    @BeforeEach
    void setUp() {
    	mockTriviaClient = mock(OpenTdbTriviaClient.class);
    	mockQuestionMapper = mock(QuestionMapper.class);
    	mockAnswerValidator = mock(AnswerValidator.class);
    	mockCacheService = mock(DefaultCacheService.class);
    	defaultTriviaService = new DefaultTriviaService(
    			mockTriviaClient, 
				mockQuestionMapper,
				mockAnswerValidator,
				mockCacheService);
    }

	
	@Test
	void givenValidResponse_whenGetAllQuestions_thenReturnMappedDtoList() {
	    QuestionFilterDto filter = new QuestionFilterDto();

	    Question question = createDefaultQuestion();
	    List<Question> questions = List.of(question);
	    
	    QuestionDto dto = createDefaultQuestionDto(question.getId());
	    List<QuestionDto> expected = List.of(dto);

	    when(mockTriviaClient.fetchQuestions(filter)).thenReturn(questions);
	    when(mockQuestionMapper.toDtoList(questions)).thenReturn(expected);

	    List<QuestionDto> result = defaultTriviaService.getAllQuestions(filter);

	    assertEquals(expected, result);
	    verify(mockTriviaClient).fetchQuestions(filter);
	    verify(mockQuestionMapper).toDtoList(questions);
	}
	
	
	@Test
	void givenResponseWithEmptyQuestionList_whenGetAllQuestions_thenReturnEmptyDtoList() {

		QuestionFilterDto filter = new QuestionFilterDto();
		List<Question> questions = Collections.emptyList();

	    when(mockTriviaClient.fetchQuestions(filter)).thenReturn(questions);

	    List<QuestionDto> result = defaultTriviaService.getAllQuestions(filter);

	    assertTrue(result.isEmpty());
	    verify(mockTriviaClient).fetchQuestions(filter);
	}
	
	@Test
	void givenClientException_whenGetAllQuestions_thenThrowException() {

	    QuestionFilterDto filter = new QuestionFilterDto();

	    when(mockTriviaClient.fetchQuestions(filter))
	            .thenThrow(new RestClientException("API call failed"));

	    assertThrows(
	            RestClientException.class,
	            () -> defaultTriviaService.getAllQuestions(filter)
	    );

	    verify(mockTriviaClient).fetchQuestions(filter);
	    verifyNoInteractions(mockQuestionMapper);
	}
	
	@Test
	void givenAnswers_whenVerifyAnswers_thenDelegateToValidator() {
		
	    List<AnswerDto> answers = List.of(new AnswerDto(1, "Paris"));

	    ResultDto result = new ResultDto(
	    		createDefaultQuestionDto(1),
	    		"Paris",
	    		"Paris");	    
	    List<ResultDto> expected = List.of(result);

	    when(mockAnswerValidator.validateAnswers(answers)).thenReturn(expected);

	    List<ResultDto> actual = defaultTriviaService.validateAnswers(answers);

	    assertEquals(expected, actual);
	    verify(mockAnswerValidator).validateAnswers(answers);
	}
	
	
    @Test
    void givenQuestionList_whenGetAllQuestions_thenCacheOriginalQuestions() {

        Question question1 = createDefaultQuestion();
        Question question2 = createQuestionWithHtml();
        QuestionFilterDto filter = new QuestionFilterDto();
        
        when(mockTriviaClient.fetchQuestions(filter)).thenReturn(Arrays.asList(question1, question2));

        defaultTriviaService.getAllQuestions(filter);

        verify(mockCacheService).put("questionsCache", question1.getId(), question1);
        verify(mockCacheService).put("questionsCache", question2.getId(), question2);
    }
    
    
    @Test
    void givenEmptyQuestionList_whenToDtoList_thenDoNotCacheAnything() {
    	
    	QuestionFilterDto filter = new QuestionFilterDto();
         
        when(mockTriviaClient.fetchQuestions(filter)).thenReturn(Collections.emptyList());
    	
    	defaultTriviaService.getAllQuestions(filter);

        verifyNoInteractions(mockCacheService);
    }

}
