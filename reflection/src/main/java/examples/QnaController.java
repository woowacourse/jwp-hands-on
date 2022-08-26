package examples;

import annotation.Controller;
import annotation.Inject;

@Controller
public class QnaController {

    private final MyQnaService qnaService;

    @Inject
    public QnaController(MyQnaService qnaService) {
        this.qnaService = qnaService;
    }
}
