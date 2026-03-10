package nl.bartvdhoven.triviamaster.service;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import nl.bartvdhoven.triviamaster.model.Question;
import nl.bartvdhoven.triviamaster.model.QuestionFilterDto;

@RestClientTest(OpenTdbTriviaClient.class)
class OpenTdbTriviaClientTest {

	@Autowired
    private OpenTdbTriviaClient triviaClient;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void givenDefaultFilter_whenFetchQuestions_thenResponseMappedToObject() {        
    
        String mockJsonResponse = """
        		{"response_code":0,
        		"results":[{"type":"multiple","difficulty":"medium","category":"History","question":"In what year did the Berlin Wall fall?","correct_answer":"1989","incorrect_answers":["1987","1991","1993"]},
        		{"type":"multiple","difficulty":"medium","category":"Geography","question":"How many countries does Spain have a land border with?","correct_answer":"5","incorrect_answers":["2","3","4"]}]}	
            """;

        this.server.expect(requestTo(startsWith("https://opentdb.com/api.php?amount=10")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(mockJsonResponse, MediaType.APPLICATION_JSON));

        QuestionFilterDto filter = new QuestionFilterDto(); 

        List<Question> questions = triviaClient.fetchQuestions(filter);
 
        assertNotNull(questions);
        assertFalse(questions.isEmpty());
        assertEquals("In what year did the Berlin Wall fall?", questions.get(0).getQuestion());
        
        this.server.verify();
    }
    
    
    @Test
    void givenDifficultyFilter_whenFetchQuestions_thenRequestUsesCorrectUri() {

        String mockJsonResponse = """
            {
              "response_code": 0,
              "results": []
            }
            """;

        QuestionFilterDto filter = new QuestionFilterDto();
        filter.setDifficulty("easy");

        server.expect(requestTo("https://opentdb.com/api.php?amount=10&difficulty=easy"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(mockJsonResponse, MediaType.APPLICATION_JSON));

        List<Question> questions = triviaClient.fetchQuestions(filter);

        assertTrue(questions.isEmpty());
        server.verify();
    }
    
    
    @Test
    void givenAmount0_whenFetchQuestions_thenReturnResponseWithEmptyQuestionList() {

        String mockJsonResponse = """
            {
              "response_code": 2,
              "results": []
            }
            """;

        QuestionFilterDto filter = new QuestionFilterDto();
        filter.setAmount(0);

        server.expect(requestTo("https://opentdb.com/api.php?amount=0"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(mockJsonResponse, MediaType.APPLICATION_JSON));

        List<Question> questions = triviaClient.fetchQuestions(filter);

        assertNotNull(questions);
        assertTrue(questions.isEmpty());

        server.verify();
    }

}
