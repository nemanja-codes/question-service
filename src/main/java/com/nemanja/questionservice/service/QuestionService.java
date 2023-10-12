package com.nemanja.questionservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nemanja.questionservice.dao.QuestionDao;
import com.nemanja.questionservice.model.Question;
import com.nemanja.questionservice.model.QuestionWrapper;
import com.nemanja.questionservice.model.Response;


@Service
public class QuestionService {

	@Autowired
	QuestionDao questionDao;
	
	public ResponseEntity<List<Question>> getAllQuestions() {
		try {
			return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
		try {
			return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

	}

	public String addQuestion(Question question) {
		questionDao.save(question);
		return "Success";
	}

	public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
		List<Integer> questions = questionDao.findRandomQuestionsByCategory(categoryName, numQuestions);
		return new ResponseEntity<> (questions,HttpStatus.OK);
	}

	public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionsIds) {
		List<QuestionWrapper> wrappers = new ArrayList<>();
		List<Question> questions = new ArrayList<>();
		
		for(Integer id:questionsIds) {
			questions.add(questionDao.findById(id).get());
		}
		for(Question question: questions) {
			QuestionWrapper questionWrapper = new QuestionWrapper();
			questionWrapper.setId(question.getId());
			questionWrapper.setQuestionTitle(question.getQuestionTitle());
			questionWrapper.setOption1(question.getOption1());
			questionWrapper.setOption2(question.getOption2());
			questionWrapper.setOption3(question.getOption3());
			questionWrapper.setOption4(question.getOption4());
			wrappers.add(questionWrapper);
		}
	
		return new ResponseEntity<>(wrappers,HttpStatus.OK);
	}

	public ResponseEntity<Integer> getScore(List<Response> responses) {
		
		int right = 0;
		
		for(Response r : responses) {
			Question question = questionDao.findById(r.getId()).get();
			if(r.getResponse().equals(question.getRightAnswer()))
				right++;
		}
		return new ResponseEntity<>(right, HttpStatus.OK);
		
	}
	

}
