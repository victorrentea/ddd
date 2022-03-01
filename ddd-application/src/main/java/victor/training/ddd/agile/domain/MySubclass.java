package victor.training.ddd.agile.domain;

public class MySubclass extends MyException {
   public MySubclass(Throwable cause, ErrorCode errorCode, String... parameters) {
      super(cause, errorCode, parameters);
   }
}
