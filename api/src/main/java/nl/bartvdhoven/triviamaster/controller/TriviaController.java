package nl.bartvdhoven.triviamaster.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.bartvdhoven.triviamaster.model.AnswerDto;
import nl.bartvdhoven.triviamaster.model.QuestionDto;
import nl.bartvdhoven.triviamaster.model.QuestionFilterDto;
import nl.bartvdhoven.triviamaster.model.ResultDto;
import nl.bartvdhoven.triviamaster.service.TriviaService;

/**
 * REST controller exposing endpoints for retrieving trivia questions
 * and validating submitted answers.
 */
@RestController
@RequestMapping("/api")
public class TriviaController {
	
	 	private final TriviaService triviaService;	 
	 	
	 	public TriviaController(TriviaService triviaService) {
	 		this.triviaService = triviaService;
	 	}
	    
	    @GetMapping("/")
	    public String home() {
	        return "Welcome to the Trivia Master API!";
	    }

	    @GetMapping("/questions")
	    public List<QuestionDto> getQuestions(@ModelAttribute QuestionFilterDto filter) {
	        return triviaService.getAllQuestions(filter);
	    }
	    	    
	    @PostMapping("/answers/validate")
	    public List<ResultDto> validateAnswers(@RequestBody List<AnswerDto> answers) {
	    	return triviaService.validateAnswers(answers);
	    }
}
