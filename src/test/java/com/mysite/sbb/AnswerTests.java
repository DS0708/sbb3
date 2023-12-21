package com.mysite.sbb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AnswerTests {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private String first_subject = "sbb가 무엇인가요?";
    void question_given(){
        this.questionRepository.save(Question.builder()
                .subject(first_subject)
                .content("sbb에 대해서 알고 싶습니다.")
                .createDate(LocalDateTime.now())
                .build()); //First question

        this.questionRepository.save(Question.builder()
                .subject("스프링부트 모델 질문입니다.")
                .content("id는 자동으로 생성되나요?")
                .createDate(LocalDateTime.now())
                .build()); //Second Question
    }

    @AfterEach
    void tearDown(){
        questionRepository.deleteAll();
        answerRepository.deleteAll();
    }

    @Test
    void testJpa_SaveAnswer(){
        //given
        question_given();
        String given_answer = "네 자동으로 생성됩니다.";

        //when
        List<Question> all_q = questionRepository.findAll();
        Question q = all_q.get(1);
        int test_question_id = q.getId();
        int test_answer_id = answerRepository.save(Answer.builder()
                .content(given_answer)
                .createDate(LocalDateTime.now())
                .question(q)
                .build()).getId();

        //then
        assertEquals(2,all_q.size());
        assertEquals(test_question_id,answerRepository.findById(test_answer_id).get().getQuestion().getId());
    }

}
