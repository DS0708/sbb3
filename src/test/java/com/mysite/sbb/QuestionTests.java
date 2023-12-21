package com.mysite.sbb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class QuestionTests {
    private String first_subject = "sbb가 무엇인가요?";

    @Autowired
    private QuestionRepository questionRepository;

    @AfterEach
    void tearDown(){
        questionRepository.deleteAll();
    }

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

    @Test
    void testJpa_save() {
        question_given();
    }

    @Test
    void testJpa_findAll() {
        question_given();

        List<Question> all = this.questionRepository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void testJpa_findById(){
        question_given();

        Optional<Question> oq = this.questionRepository.findById(1);
//		Optional은 null 처리를 유연하게 처리하기 위해 사용하는 클래스로 위와 같이 isPresent로 null이 아닌지를 확인한 후에 get으로 실제 Question 객체 값을 얻어야 한다.
        if(oq.isPresent()) {
            Question q = oq.get();
            assertEquals("sbb가 무엇인가요?", q.getSubject());
        }
    }
    @Test
    void testJpa_findBySubject(){
        question_given();

        Question q = this.questionRepository.findBySubject(first_subject);
        assertEquals(first_subject, q.getSubject());
    }

    @Test
    void testJpa_findBySubjectAndContent(){
        question_given();

        Question q = this.questionRepository.findBySubjectAndContent(
                "sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    @Test
    void testJpa_findBySubjectLike(){
        question_given();

        List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
        Question q = qList.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    @Test
    void testJpa_update(){
        question_given();

        String update_subject = "수정된 제목";
        Question q = this.questionRepository.findBySubject(first_subject);
        q.updateSubject(update_subject);
        this.questionRepository.save(q);

        Question update_q = this.questionRepository.findBySubject(update_subject);
        assertEquals(update_subject,update_q.getSubject());
    }

    @Test
    void testJpa_delete(){
        question_given();

        assertEquals(2, this.questionRepository.count());
        Question q = this.questionRepository.findBySubject(first_subject);
        this.questionRepository.delete(q);
        assertEquals(1, this.questionRepository.count());
    }
}
