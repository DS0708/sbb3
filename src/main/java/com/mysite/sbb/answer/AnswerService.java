package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    public void create(Question question,String content){
        this.answerRepository.save(Answer.builder()
                .content(content)
                .createDate(LocalDateTime.now())
                .question(question)
                .build());
    }
}
