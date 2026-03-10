package nl.bartvdhoven.triviamaster.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Component;

import nl.bartvdhoven.triviamaster.model.Question;
import nl.bartvdhoven.triviamaster.model.QuestionDto;

/**
 * Responsible for converting {@link Question} objects to {@link QuestionDto}.
 *
 * During the mapping process HTML entities returned by the OpenTDB API
 * are decoded and the answer options are shuffled.
 */
@Component
public class QuestionMapper {
	
	/**
	 * Converts a {@link Question} object to a {@link QuestionDto}.
	 *
	 * HTML entities returned by the OpenTDB API are decoded and the
	 * answer options are shuffled so the correct answer does not always
	 * appear in the same position.
	 *
	 * @param question the question to convert
	 * @return the mapped question DTO
	 */
	public QuestionDto toDto(Question question) {
		
		// decode html to plain text
		String decodedQuestion = StringEscapeUtils.unescapeHtml4(question.getQuestion());
		String decodedCategory = StringEscapeUtils.unescapeHtml4(question.getCategory());
		String decodedCorrectAnswer = StringEscapeUtils.unescapeHtml4(question.getCorrectAnswer());
		
		// create and shuffle list with all answers
		List<String> decodedAnswers = new ArrayList<>();
		decodedAnswers.add(decodedCorrectAnswer);
		decodedAnswers.addAll(question.getIncorrectAnswers().stream()
							   .map(StringEscapeUtils::unescapeHtml4)
							   .collect(Collectors.toList()));
		Collections.shuffle(decodedAnswers);
		
		return new QuestionDto(
				question.getId(),
				question.getType(),
				question.getDifficulty(),
				decodedCategory,
				decodedQuestion,
				decodedAnswers
		);
	}
		
	/**
	 * Converts a list of {@link Question} objects to {@link QuestionDto}.
	 *
	 * @param questions the questions to convert
	 * @return list of mapped question DTOs
	 */
	public List<QuestionDto> toDtoList(List<Question> questions) {
			
	    return questions.stream()
	                    .map(question -> this.toDto(question))
	                    .collect(Collectors.toList());
	}

	
}

