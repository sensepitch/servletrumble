package org.sensepitch.servletrumble;

import com.google.gson.Gson;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Jens Wilke
 */
public class Recorder {

  private ConcurrentMap<String, Object> mainMap;
  private ConcurrentMap<String, Object> map;

  public static Recorder recordSingleEvent(String type, ServletRequest req) {
    ConcurrentMap<String, Object> _main = mainRecorderMap(req);
    ConcurrentHashMap<String, Object> m = new ConcurrentHashMap<String, Object>();
    Object res = _main.putIfAbsent(type, m);
    if (res != null) {
      throw new IllegalStateException("Event " + type + "happened twice");
    }
    return new Recorder(_main, m);
  }

  public static Recorder recordMultipleEvent(String type, ServletRequest req) {
    ConcurrentMap<String, Object> _main = mainRecorderMap(req);
    _main.putIfAbsent(type, new ConcurrentHashMap<String, Object>());
    ConcurrentMap<String, Object> invocation = (ConcurrentMap<String, Object>) _main.get(type);
    int invocationCount = 1;
    while (invocation.putIfAbsent(Integer.toString(invocationCount), new ConcurrentHashMap<String, Object>()) != null) {
      invocationCount++;
    }
    return new Recorder(_main, (ConcurrentMap<String, Object>) invocation.get(Integer.toString(invocationCount)));
  }

  /**
   * Create a recording map as request attribute
   */
  private static ConcurrentMap<String, Object> mainRecorderMap(final ServletRequest req) {
    ConcurrentMap<String, Object> main =
      (ConcurrentMap<String, Object>) req.getAttribute(Recorder.class.getName());
    if (main == null) {
      main = new ConcurrentHashMap<String, Object>();
      req.setAttribute(Recorder.class.getName(), main);
    }
    return main;
  }

  public Recorder(final ConcurrentMap<String, Object> _mainMap, final ConcurrentMap<String, Object> _map) {
    mainMap = _mainMap;
    map = _map;
  }

  public Recorder type(Class<?> t) {
    map.put("type", t.getName());
    return this;
  }

  public Recorder request(ServletRequest req0) {
    Map<String, String> info = new HashMap<String, String>();
    if (req0 instanceof HttpServletRequest) {
      HttpServletRequest req = (HttpServletRequest) req0;
      info.put("contextPath", req.getContextPath());
      info.put("servletPath", req.getServletPath());
      info.put("pathInfo", req.getPathInfo());
      info.put("requestURI", req.getRequestURI());
      info.put("queryString", req.getQueryString());
    }
    map.put("request", info);
    return this;
  }

  public Map<String, Object> toMap() {
    return mainMap;
  }

  public String toJson() {
    Gson gson = new Gson();
    return gson.toJson(mainMap);
  }

}
