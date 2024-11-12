package hello.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView entityNotFoundException(EntityNotFoundException ex, Model model) {
        ModelAndView mav = new ModelAndView("error/404");
        model.addAttribute("errorMessage", ex.getMessage());
        mav.setStatus(HttpStatus.NOT_FOUND);
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView allException(Exception ex, Model model) {
        ModelAndView mav = new ModelAndView("error/500");
        model.addAttribute("errorMessage", ex.getMessage());
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return mav;
    }
}
