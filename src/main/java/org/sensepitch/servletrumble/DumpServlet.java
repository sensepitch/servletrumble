package org.sensepitch.servletrumble;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Jens Wilke
 */
public class DumpServlet extends HttpServlet {

  @Override
  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
    Recorder r = Recorder.recordSingleEvent("/dump", req)
      .request(req);
    resp.setContentType("application/json");
    resp.getWriter().print(r.toJson());
  }

}
