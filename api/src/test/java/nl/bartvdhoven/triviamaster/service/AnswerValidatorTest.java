package nl.bartvdhoven.triviamaster.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.bartvdhoven.triviamaster.exception.CachedObjectNotFoundException;
import nl.bartvdhoven.triviamaster.model.AnswerDto;
import nl.bartvdhoven.triviamaster.model.Question;
import nl.bartvdhoven.triviamaster.model.QuestionDto;
import nl.bartvdhoven.triviamaster.model.ResultDto;

import static nl.bartvdhoven.triviamaster.test.TestDataFactory.createDefaultQuestion;
import static nl.bartvdhoven.triviamaster.test.TestDataFactory.createDefaultQuestionDto;
import static nl.bartvdhoven.triviamaster.test.TestDataFactory.createQuestionWithHtml;
import static nl.bartvdhoven.triviamaster.test.TestDataFactory.createQuestionDtoWithHtml;

class AnswerValidatorTest {
	
    private DefaultCacheService mockCacheService;
    private QuestionMapper mockQuestionMapper;
    private AnswerValidator answerValidator;
	

    @BeforeEach
    void setUp() {
    	mockCacheService = mock(DefaultCacheService.class);
    	mockQuestionMapper = mock(QuestionMapper.class);
    	answerValidator = new AnswerValidator(mockQuestionMapper, mockCacheService);
    }

    @Test
    void givenMatchingAnswer_whenValidateAnswers_thenReturnTrueResult() {
    	
        Question question = createDefaultQuestion();
        AnswerDto answer = new AnswerDto(question.getId(), "Correct");
        QuestionDto questionDto = createDefaultQuestionDto(question.getId());

        when(mockCacheService.get("questionsCache", question.getId(), Question.class)).thenReturn(question);
        when(mockQuestionMapper.toDto(question)).thenReturn(questionDto);

        List<ResultDto> results = answerValidator.validateAnswers(Arrays.asList(answer));

        assertEquals(1, results.size());
        assertEquals("Correct", results.get(0).getSubmittedAnswer());
        assertEquals("Correct", results.get(0).getCorrectAnswer());
        assertTrue(results.get(0).getResult());
    }
    
    @Test
    void givenWrongAnswer_whenValidateAnswers_thenReturnFalseResult() {
    	
        Question question = createDefaultQuestion();
        AnswerDto answer = new AnswerDto(question.getId(), "Wrong1");
        QuestionDto questionDto = createDefaultQuestionDto(question.getId());

        when(mockCacheService.get("questionsCache", question.getId(), Question.class)).thenReturn(question);
        when(mockQuestionMapper.toDto(question)).thenReturn(questionDto);

        List<ResultDto> results = answerValidator.validateAnswers(Arrays.asList(answer));

        assertEquals(1, results.size());
        assertEquals("Wrong1", results.get(0).getSubmittedAnswer());
        assertEquals("Correct", results.get(0).getCorrectAnswer());
        assertFalse(results.get(0).getResult());
    }
    
    @Test
    void givenHtmlEncodedCorrectAnswer_whenValidateAnswers_thenDecodeCorrectAnswer() {
      
        // question correct answer with html encoding - &quot;Super Smash Bros. for Nintendo 3DS&quot; 
        Question question = createQuestionWithHtml();
        AnswerDto answer = new AnswerDto(question.getId(), "\"Super Smash Bros. for Nintendo 3DS\"");        
        
        QuestionDto questionDto = createQuestionDtoWithHtml(question.getId());
        
        when(mockCacheService.get("questionsCache", question.getId(), Question.class)).thenReturn(question);
        when(mockQuestionMapper.toDto(question)).thenReturn(questionDto);

        List<ResultDto> results = answerValidator.validateAnswers(Arrays.asList(answer));

        assertEquals(1, results.size());
        assertEquals("\"Super Smash Bros. for Nintendo 3DS\"", results.get(0).getCorrectAnswer());
        assertTrue(results.get(0).getResult());
    }
    
    @Test
    void givenMultipleAnswers_whenValidateAnswers_thenReturnResultsForAllAnswers() {
    	
    	Question question1 = createDefaultQuestion();
    	Question question2 = createDefaultQuestion();
    	Question question3 = createQuestionWithHtml();
    	Question question4 = createQuestionWithHtml();
    	
    	AnswerDto answer1 = new AnswerDto(question1.getId(), "Correct"); 
    	AnswerDto answer2 = new AnswerDto(question2.getId(), "Wrong1"); 
    	AnswerDto answer3 = new AnswerDto(question3.getId(), "\"Super Smash Bros. for Nintendo 3DS\""); 
    	AnswerDto answer4 = new AnswerDto(question4.getId(), "Assume temperature is 25°C.");
    	
    	QuestionDto questionDto1 = createQuestionDtoWithHtml(question1.getId());
    	QuestionDto questionDto2 = createQuestionDtoWithHtml(question2.getId());
    	QuestionDto questionDto3 = createQuestionDtoWithHtml(question3.getId());
    	QuestionDto questionDto4 = createQuestionDtoWithHtml(question4.getId());
    	
        when(mockCacheService.get("questionsCache", question1.getId(), Question.class)).thenReturn(question1);
        when(mockCacheService.get("questionsCache", question2.getId(), Question.class)).thenReturn(question2);
        when(mockCacheService.get("questionsCache", question3.getId(), Question.class)).thenReturn(question3);
        when(mockCacheService.get("questionsCache", question4.getId(), Question.class)).thenReturn(question4);
        
        when(mockQuestionMapper.toDto(question1)).thenReturn(questionDto1);
        when(mockQuestionMapper.toDto(question2)).thenReturn(questionDto2);
        when(mockQuestionMapper.toDto(question3)).thenReturn(questionDto3);
        when(mockQuestionMapper.toDto(question4)).thenReturn(questionDto4);
    	
    	List<ResultDto> results = answerValidator.validateAnswers(Arrays.asList(answer1, answer2, answer3, answer4));
    	
        assertEquals(4, results.size());

        assertEquals("Correct", results.get(0).getCorrectAnswer());
        assertEquals("Correct", results.get(0).getSubmittedAnswer());
        assertTrue(results.get(0).getResult());

        assertEquals("Correct", results.get(1).getCorrectAnswer());
        assertEquals("Wrong1", results.get(1).getSubmittedAnswer());
        assertFalse(results.get(1).getResult());

        assertEquals("\"Super Smash Bros. for Nintendo 3DS\"", results.get(2).getCorrectAnswer());
        assertEquals("\"Super Smash Bros. for Nintendo 3DS\"", results.get(2).getSubmittedAnswer());
        assertTrue(results.get(2).getResult());

        assertEquals("\"Super Smash Bros. for Nintendo 3DS\"", results.get(3).getCorrectAnswer());
        assertEquals("Assume temperature is 25°C.", results.get(3).getSubmittedAnswer());
        assertFalse(results.get(3).getResult());
    	
    }
    
    @Test
    void givenMissingQuestion_whenValidateAnswers_thenThrowQuestionNotFoundException() {
       
    	AnswerDto answer = new AnswerDto(777, "Answer");

        when(mockCacheService.get("questionsCache", 777, Question.class))
            .thenThrow(new CachedObjectNotFoundException(99L, "Wrapper is null"));

        assertThrows(
            CachedObjectNotFoundException.class,
            () -> answerValidator.validateAnswers(List.of(answer))
        );
    }
    
}
