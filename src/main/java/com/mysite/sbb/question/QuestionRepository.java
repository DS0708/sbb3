package com.mysite.sbb.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findBySubject(String subject);
    Question findBySubjectAndContent(String subject, String content);
    List<Question> findBySubjectLike(String subject);
    Page<Question> findAll(Pageable pageable);
    Page<Question> findAll(Specification<Question> spec, Pageable pageable); //keyword 검색기능
    @Query("select "
            + "distinct q "
            + "from Question q "
            + "left outer join SiteUser u1 on q.author=u1 "
            + "left outer join Answer a on a.question=q "
            + "left outer join SiteUser u2 on a.author=u2 "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.username like %:kw% "
            + "   or a.content like %:kw% "
            + "   or u2.username like %:kw% ")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
    //@Query에 매개변수로 전달할 kw 문자열은 @Param 애너테이션을 사용해야 하며 :kw로 참조
    /*
    * @Query는 반드시 테이블 기준이 아닌 엔티티 기준으로 작성해야 한다.
    * 즉, site_user와 같은 테이블명 대신 SiteUser처럼 엔티티명을 사용해야 하고,
    * 조인문에서 보듯이 q.author_id=u1.id와 같은 컬럼명 대신 q.author=u1처럼
    * 엔티티의 속성명을 사용해야 한다.
    * */
}
