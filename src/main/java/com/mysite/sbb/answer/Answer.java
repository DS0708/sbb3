package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @ManyToOne
    private Question question;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;

    @Builder
    public Answer(String content, LocalDateTime createDate, Question question, SiteUser author){
        this.content = content;
        this.createDate = createDate;
        this.question = question;
        this.author = author;
    }

    public void update(String content){
        this.content = content;
        this.modifyDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

}
