
package nl.bartvdhoven.triviamaster.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.bartvdhoven.triviamaster.model.Question;
import nl.bartvdhoven.triviamaster.model.QuestionDto;

import static nl.bartvdhoven.triviamaster.test.TestDataFactory.createDefaultQuestion;
import static nl.bartvdhoven.triviamaster.test.TestDataFactory.createQuestionWithHtml;

class QuestionMapperTest {
	
    private QuestionMapper questionMapper;
    
    @BeforeEach
    void setUp() {
    	questionMapper = new QuestionMapper();
    }

    @Test
    void givenQuestion_whenToDto_thenAnswersContainCorrectAndIncorrectAnswers() {

    	Question question = createDefaultQuestion();
    	
        QuestionDto dto = questionMapper.toDto(question);

        assertEquals(4, dto.getAnswers().size());
        assertTrue(dto.getAnswers().contains("Correct"));
        assertTrue(dto.getAnswers().contains("Wrong1"));
        assertTrue(dto.getAnswers().contains("Wrong2"));
        assertTrue(dto.getAnswers().contains("Wrong3"));
        
    }
    
    @Test
    void givenQuestion_whenToDto_thenBasicFieldsAreMapped() {
        Question question = createDefaultQuestion();

        QuestionDto dto = questionMapper.toDto(question);

        assertEquals(question.getId(), dto.getId());
        assertEquals(question.getType(), dto.getType());
        assertEquals(question.getDifficulty(), dto.getDifficulty());
    }
    
    @Test
    void givenQuestionWithNullIncorrectAnswers_whenToDto_thenReturnOnlyCorrectAnswer() {
        Question question = new Question(
                "multiple",
                "easy",
                "General Knowledge",
                "What is correct?",
                "Correct",
                null
        );

        QuestionDto dto = questionMapper.toDto(question);

        assertEquals(1, dto.getAnswers().size());
        assertTrue(dto.getAnswers().contains("Correct"));
    }
    
    @Test
    void givenQuestionWithHTML_whenToDto_thenFieldsAreDecoded() {

        Question question = createQuestionWithHtml();

        QuestionDto dto = questionMapper.toDto(question);        
        
        System.out.println(dto.getAnswers());

        assertEquals("Entertainment: Cartoon & Animations", dto.getCategory());
        assertEquals("What's the Team Fortress 2 Scout's city of origin?", dto.getQuestion());
        assertTrue(dto.getAnswers().contains("\"Super Smash Bros. for Nintendo 3DS\""));
        assertTrue(dto.getAnswers().contains("Assume temperature is 25°C."));
    }
    

    @Test
    void givenEmptyQuestionList_whenToDtoList_thenReturnEmptyList() {
    	
        List<QuestionDto> result = questionMapper.toDtoList(Arrays.asList());
        assertTrue(result.isEmpty());
    }

}
