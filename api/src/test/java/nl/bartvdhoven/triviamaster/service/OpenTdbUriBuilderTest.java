package nl.bartvdhoven.triviamaster.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import nl.bartvdhoven.triviamaster.model.QuestionFilterDto;

class OpenTdbUriBuilderTest {

	@Test
	void givenDefaultFilter_whenBuildUri_thenAmountIs10() {
		
        QuestionFilterDto filter = new QuestionFilterDto();
 
        String actualUri = OpenTdbUriBuilder.buildUri(filter);
        String expectedUri = "?amount=10";
        
        assertEquals(expectedUri, actualUri);
	}
	
    @Test
    void givenAllFilters_whenBuildUri_thenFiltersIncluded() {
    	
        QuestionFilterDto filter = new QuestionFilterDto();        
        filter.setAmount(5);
        filter.setType("multiple");
        filter.setDifficulty("easy");
        filter.setCategory(21);

        String actualUri = OpenTdbUriBuilder.buildUri(filter);
        String expectedUri = "?amount=5&category=21&difficulty=easy&type=multiple";
        
        assertEquals(expectedUri, actualUri);
    }
	
    
    @Test
    void givenSomeFilters_whenBuildUri_thenEmptyFiltersNotIncluded() {
    	
        QuestionFilterDto filter = new QuestionFilterDto();        
        filter.setAmount(3);
        filter.setDifficulty("easy");

        String actualUri = OpenTdbUriBuilder.buildUri(filter);
        String expectedUri = "?amount=3&difficulty=easy";
        
        assertEquals(expectedUri, actualUri);

    }

}
