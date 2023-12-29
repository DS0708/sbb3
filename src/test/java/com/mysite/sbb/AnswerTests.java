package com.mysite.sbb;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AnswerTests {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @AfterEach
    void tearDown(){
        questionRepository.deleteAll();
        answerRepository.deleteAll();
    }

    private String first_subject = "sbb가 무엇인가요?";
    void question_given(){
        this.questionRepository.save(Question.builder()
                .subject(first_subject)
                .content("sbb에 대해서 알고 싶습니다.")
                .createDate(LocalDateTime.now())
                .build()); //First question

        Question secondQuestion = this.questionRepository.save(Question.builder()
                .subject("스프링부트 모델 질문입니다.")
                .content("id는 자동으로 생성되나요?")
                .createDate(LocalDateTime.now())
                .build()); //Second Question

        this.answerRepository.save(Answer.builder()
                .content("네 자동으로 생성됩니다.")
                .createDate(LocalDateTime.now())
                .question(secondQuestion)
                .build()); // Answer for the second question
    }


    @Test
    void testJpa_SaveAnswer(){
        //given
        question_given();
        String given_answer = "네 자동으로 생성됩니다.";

        //when
        Question q = this.questionRepository.findById(2).get();
        List<Answer> answerList = q.getAnswerList();

        //then
        assertEquals(1,answerList.size());
        assertEquals(given_answer,answerList.get(0).getContent());
    }

}
