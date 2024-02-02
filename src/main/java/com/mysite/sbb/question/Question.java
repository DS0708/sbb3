package com.mysite.sbb.question;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER) //test code에서 DB세션이 끊어지는 문제를 해결하기 위해 fetch = FetchType.EAGER
    private List<Answer> answerList;
//    @OneToMany 애너테이션에 사용된 mappedBy는 참조 엔티티의 속성명을 의미한다. 즉, Answer 엔티티에서 Question 엔티티를 참조한 속성명 question을 mappedBy에 전달해야 한다.
//    질문 하나에는 여러개의 답변이 작성될 수 있다. 이때 질문을 삭제하면 그에 달린 답변들도 모두 함께 삭제하기 위해서 @OneToMany의 속성으로 cascade = CascadeType.REMOVE를 사용했다.

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;

    @Builder
    public Question(String subject, String content, LocalDateTime createDate, SiteUser author){
        this.subject = subject;
        this.content = content;
        this.createDate = createDate;
        this.author = author;
    }
    public void updateSubject(String subject){
        this.subject = subject;
    }

    public void update(String subject, String content){
        this.subject = subject;
        this.content = content;
        this.modifyDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}
