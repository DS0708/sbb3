package com.mysite.sbb.question;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public void create(String subject, String content, SiteUser author){
        this.questionRepository.save(Question.builder()
                .subject(subject)
                .content(content)
                .createDate(LocalDateTime.now())
                .author(author)
                .build());
    }

    public Page<Question> getList(int page){
        // 역순정렬
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        // desc는 내림차순을 의미하고, asc는 오름차순을 의미한다.

        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));
//      page는 조회할 페이지의 번호이고 10은 한 페이지에 보여 줄 게시물의 개수를 의미

        return this.questionRepository.findAll(pageable);
    }

    @Transactional
    public void modify(Question question, String subject, String content) {
        question.update(subject,content);
    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

}
