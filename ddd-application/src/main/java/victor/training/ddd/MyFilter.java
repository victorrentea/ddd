package victor.training.ddd;

import javax.servlet.*;
import java.io.IOException;

public class MyFilter implements Filter {
   @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//      request.get
      chain.doFilter(request, response);

   }
}
