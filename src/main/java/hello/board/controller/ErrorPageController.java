package hello.board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class ErrorPageController {

    @RequestMapping("/error/404")
    public String errorPage404() {
        log.info("errorPage404");
        return "error/404";
    }

    @RequestMapping("/error/500")
    public String errorPage500() {
        log.info("errorPage500");
        return "error/500";
    }
}
