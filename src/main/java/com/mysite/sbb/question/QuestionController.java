package com.mysite.sbb.question;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Slf4j
@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw){
//        Model 객체는 자바 클래스와 템플릿 간의 연결고리 역할을 한다. Model 객체에 값을 담아두면 템플릿에서 그 값을 사용할 수 있다.
//        List<Question> questionList = this.questionService.getList();
//        model.addAttribute("questionList",questionList);

        //logging
        log.info("page:{}, kw:{}", page, kw);
        /*
        *  로그 레벨(log level)은 다음과 같이 6단계로 구성되며,
        *  각 단계의 로그는 log.trace, log.debug, log.info,
        *  log.warn, log.error, log.fatal과 같이 출력
        *  log level 순서 : TRACE < DEBUG < INFO < WARN < ERROR < FATAL
         * */
        /*
        * 만약 application-prod.properties 파일에
        * logging.level.root=info로 설정하면
        * TRACE, DEBUG 로그는 출력되지 않고 INFO 이상의 로그만 출력.
        * logging.level.root의 기본값은 info
        * */


        //Paging
        Page<Question> paging = this.questionService.getList(page,kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw",kw);
        //화면에서 입력한 검색어를 화면에 그대로 유지하기 위해 model.addAttribute("kw", kw)로 kw값을 저장

        return "question_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question",question);
        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()") //@PreAuthorize("isAuthenticated()") 애너테이션이 붙은 메서드는 로그인한 경우에만 실행된다.
    @GetMapping("/create")
//    public String questionCreate(){
//        return "question_form";
//    }
    public String questionCreate(QuestionForm questionForm){
//    QuestionForm과 같이 매개변수로 바인딩한 객체는 Model 객체로 전달하지 않아도 템플릿에서 사용할 수 있다.
        return "question_form";
    }
    /* 템플릿의 form 태그에 th:object 속성을 추가했으므로
    QuestionController의 GetMapping으로 매핑한 메서드도 다음과 같이 변경해야 오류가 발생하지 않는다.
    왜냐하면 question_form.html은 [질문 등록하기] 버튼을 통해
    GET 방식으로 URL이 요청되더라도 th:object에 의해 QuestionForm 객체가 필요하기 때문이다. */

    @PreAuthorize("isAuthenticated()")
    @PostMapping("create")
//    public String questionCreate(@RequestParam(value = "subject") String subject,
//                                 @RequestParam(value = "content") String content){
//
//        questionService.create(subject,content);
//        return "redirect:/question/list";
//    }
    public String questionCreate(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult, //BindingResult 매개변수는 항상 @Valid 매개변수 바로 뒤에 위치해야 한다
                                 Principal principal){
        SiteUser siteUser = this.userService.getUser(principal.getName());

        if (bindingResult.hasErrors()){
            return "question_form";
        }
        this.questionService.create(questionForm.getSubject(),questionForm.getContent(),siteUser);
        return "redirect:/question/list";
    }
    /*questionCreate 메서드의 매개변수를 subject, content 대신 QuestionForm 객체로 변경했다.
    subject, content 항목을 지닌 폼이 전송되면 QuestionForm의 subject, content 속성이 자동으로 바인딩된다.
    이렇게 이름이 동일하면 함께 연결되어 묶이는 것이 바로 폼의 바인딩 기능이다. */

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm,
                                 @PathVariable("id") Integer id,
                                 Principal principal){
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        /*
        * 수정할 질문의 제목과 내용을 화면에 보여 주기 위해 questionForm 객체에 id값으로
        * 조회한 질문의 제목(subject)과 내용(object)의 값을 담아서 템플릿으로 전달했다.
        * */

        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(@PathVariable("id") Integer id,
                                 Principal principal){
        Question question = this.questionService.getQuestion(id);
        if(!principal.getName().equals(question.getAuthor().getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(@PathVariable("id") Integer id,
                               Principal principal){
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question,siteUser);

        return String.format("redirect:/question/detail/%s", id);
    }
}
