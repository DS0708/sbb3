package com.mysite.sbb.question;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

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
                .createDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .author(author)
                .build());
    }

    public Page<Question> getList(int page, String kw){
        // 역순정렬
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        // desc는 내림차순을 의미하고, asc는 오름차순을 의미한다.

        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));
//      page는 조회할 페이지의 번호이고 10은 한 페이지에 보여 줄 게시물의 개수를 의미

        Specification<Question> spec = search(kw);

        //return this.questionRepository.findAll(spec,pageable);
        return this.questionRepository.findAllByKeyword(kw,pageable); // Specification 대신 쿼리를 직접 작성하여 검색 기능을 구현
    }

    @Transactional
    public void modify(Question question, String subject, String content) {
        question.update(subject,content);
    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
        /*
        select
            distinct q.id,
            q.author_id,
            q.content,
            q.create_date,
            q.modify_date,
            q.subject
        from question q
        left outer join site_user u1 on q.author_id=u1.id
        left outer join answer a on q.id=a.question_id
        left outer join site_user u2 on a.author_id=u2.id
        where
            q.subject like '%kw%'
            or q.content like '%kw%'
            or u1.username like '%kw%'
            or a.content like '%kw%'
            or u2.username like '%kw%'
        */
    }

}
