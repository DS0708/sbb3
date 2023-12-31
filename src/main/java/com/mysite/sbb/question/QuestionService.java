package com.mysite.sbb.question;

import com.mysite.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public List<Question> getList(){
        return this.questionRepository.findAll();
    }

    public Question getQuestion(Integer id){
        Optional<Question> question = this.questionRepository.findById(id);
        if(question.isPresent()){
            return question.get();
        }else {
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content){
        this.questionRepository.save(Question.builder()
                .subject(subject)
                .content(content)
                .createDate(LocalDateTime.now())
                .build());
    }

//    public void insert_test() {
//        this.questionRepository.save(Question.builder()
//                .subject("sbb가 무엇인가요?")
//                .content("sbb에 대해서 알고 싶습니다.")
//                .createDate(LocalDateTime.now())
//                .build());
//    }
}
