package victor.training.ddd.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import victor.training.ddd.common.MyException.ErrorCode;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
   private final MessageSource messageSource;

   @ExceptionHandler(MyException.class)
   @ResponseStatus //500
   public String handleMyException(HttpServletRequest request, MyException myException) {
      return translateError(myException, myException.getErrorCode(), myException.getParameters(), request);
   }

   @ExceptionHandler(Exception.class)
   @ResponseStatus // 500
   public String handleException(HttpServletRequest request, Exception exception) throws Exception {
      return translateError(exception, ErrorCode.GENERAL, new String[]{exception.getMessage()}, request);
   }

   private String translateError(Throwable throwable, ErrorCode errorCode, String[] parameters, HttpServletRequest request) {
      String messageKey = "error." + errorCode;
      String userMessage = messageSource.getMessage(messageKey, parameters, getCurrentUserLocale(request));
      log.error(String.format("Error occurred [%s]: %s", errorCode, userMessage), throwable);
      return userMessage;
   }

   private Locale getCurrentUserLocale(HttpServletRequest request) {
      return request.getLocale(); // or from database
   }
}