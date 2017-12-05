<%@ page import="org.sensepitch.servletrumble.Recorder" %><%@
    page import="com.google.gson.Gson" %><%--

--%><%

    Recorder r = Recorder.recordSingleEvent("/dump.jsp", request)
            .request(request);
    Gson gson = new Gson();
    String json = gson.toJson(r.toMap());

%><%= json %>