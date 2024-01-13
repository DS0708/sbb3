package com.mysite.sbb.question;

import com.mysite.sbb.answer.AnswerForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page){
//        Model 객체는 자바 클래스와 템플릿 간의 연결고리 역할을 한다. Model 객체에 값을 담아두면 템플릿에서 그 값을 사용할 수 있다.
//        List<Question> questionList = this.questionService.getList();
//        model.addAttribute("questionList",questionList);

        //Paging
        Page<Question> paging = this.questionService.getList(page);
        model.addAttribute("paging", paging);

        return "question_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question",question);
        return "question_detail";
    }

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

    @PostMapping("create")
//    public String questionCreate(@RequestParam(value = "subject") String subject,
//                                 @RequestParam(value = "content") String content){
//
//        questionService.create(subject,content);
//        return "redirect:/question/list";
//    }
    public String questionCreate(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult){  //BindingResult 매개변수는 항상 @Valid 매개변수 바로 뒤에 위치해야 한다
        if (bindingResult.hasErrors()){
            return "question_form";
        }
        this.questionService.create(questionForm.getSubject(),questionForm.getContent());
        return "redirect:/question/list";
    }
    /*questionCreate 메서드의 매개변수를 subject, content 대신 QuestionForm 객체로 변경했다.
    subject, content 항목을 지닌 폼이 전송되면 QuestionForm의 subject, content 속성이 자동으로 바인딩된다.
    이렇게 이름이 동일하면 함께 연결되어 묶이는 것이 바로 폼의 바인딩 기능이다. */


//    @GetMapping("/question/insert_test")
//    public String insert_test(){
//        questionService.insert_test();
//
//        return "redirect:/";
//    }
}
