package org.sensepitch.servletrumble;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Jens Wilke
 */
public class PrefixStripFilter implements Filter {

  public static final String PREFIX = "/prefixStripFilter";

  public void init(FilterConfig _filterConfig) throws ServletException {
  }

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
    throws IOException, ServletException {
    Recorder.recordMultipleEvent("filter", req)
      .type(this.getClass())
      .request(req);
    String uri = ((HttpServletRequest) req).getRequestURI();
    if (!uri.startsWith(PREFIX)) {
      throw new IllegalArgumentException("Prefix " + PREFIX + " expected, got: " + uri);
    }
    req.getRequestDispatcher(uri.substring(PREFIX.length())).forward(req, res);
  }

  public void destroy() {

  }

}
