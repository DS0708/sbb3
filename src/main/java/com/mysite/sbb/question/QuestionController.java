package com.mysite.sbb.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/list")
    public String list(Model model){
//        Model 객체는 자바 클래스와 템플릿 간의 연결고리 역할을 한다. Model 객체에 값을 담아두면 템플릿에서 그 값을 사용할 수 있다.
        List<Question> questionList = this.questionService.getList();
        model.addAttribute("questionList",questionList);

        return "question_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question",question);
        return "question_detail";
    }

    @GetMapping("/question/insert_test")
    public String insert_test(){
        questionService.insert_test();

        return "redirect:/";
    }
}
