package com.jspptd.postal.collectserver.config.session;

import org.springframework.session.Session;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by LOG on 2016/9/6.
 * 此类用于同时支持Header模式的Session和Cookie模式的Session的实现类
 * 但是遵循移动现行，即以Header Session为先
 * 现在移动框架有些httpclient api可以支持cookie
 */
public class AllHttpSessionStrategy implements HttpSessionStrategy {
    private String headerName = "x-security-token";
    static final String DEFAULT_ALIAS = "0";
    private static final Pattern ALIAS_PATTERN = Pattern.compile("^[\\w-]{1,50}$");
    private CookieSerializer cookieSerializer = new DefaultCookieSerializer();
    static final String DEFAULT_SESSION_ALIAS_PARAM_NAME = "_s";
    private String sessionParam = DEFAULT_SESSION_ALIAS_PARAM_NAME;
    private static final String SESSION_IDS_WRITTEN_ATTR = CookieHttpSessionStrategy.class
            .getName().concat(".SESSIONS_WRITTEN_ATTR");


    public String getRequestedSessionId(HttpServletRequest request) {

        String sessionId = request.getHeader(this.headerName);
        if (sessionId != null && !sessionId.isEmpty()) {
            return sessionId;
        }

        Map<String, String> sessionIds = getSessionIds(request);
        String sessionAlias = getCurrentSessionAlias(request);
        return sessionIds.get(sessionAlias);
    }

    public void onNewSession(Session session, HttpServletRequest request,
                             HttpServletResponse response) {

        if (session.getAttributeNames().size() <= 0) {
            return;
        }
        response.setHeader(this.headerName, session.getId());

        Set<String> sessionIdsWritten = getSessionIdsWritten(request);
        if (sessionIdsWritten.contains(session.getId())) {
            return;
        }
        sessionIdsWritten.add(session.getId());

        Map<String, String> sessionIds = getSessionIds(request);
        String sessionAlias = getCurrentSessionAlias(request);
        sessionIds.put(sessionAlias, session.getId());

        String cookieValue = createSessionCookieValue(sessionIds);
        this.cookieSerializer
                .writeCookieValue(new CookieSerializer.CookieValue(request, response, cookieValue));
    }


    public void onInvalidateSession(HttpServletRequest request,
                                    HttpServletResponse response) {
        response.setHeader(this.headerName, "");

        Map<String, String> sessionIds = getSessionIds(request);
        String requestedAlias = getCurrentSessionAlias(request);
        sessionIds.remove(requestedAlias);

        String cookieValue = createSessionCookieValue(sessionIds);
        this.cookieSerializer
                .writeCookieValue(new CookieSerializer.CookieValue(request, response, cookieValue));


    }

    public void setHeaderName(String headerName) {
        Assert.notNull(headerName, "headerName cannot be null");
        this.headerName = headerName;
    }


    public Map<String, String> getSessionIds(HttpServletRequest request) {
        List<String> cookieValues = this.cookieSerializer.readCookieValues(request);
        String sessionCookieValue = cookieValues.isEmpty() ? ""
                : cookieValues.iterator().next();
        Map<String, String> result = new LinkedHashMap<String, String>();
        StringTokenizer tokens = new StringTokenizer(sessionCookieValue, " ");
        if (tokens.countTokens() == 1) {
            result.put(DEFAULT_ALIAS, tokens.nextToken());
            return result;
        }
        while (tokens.hasMoreTokens()) {
            String alias = tokens.nextToken();
            if (!tokens.hasMoreTokens()) {
                break;
            }
            String id = tokens.nextToken();
            result.put(alias, id);
        }
        return result;
    }

    public String getCurrentSessionAlias(HttpServletRequest request) {
        if (this.sessionParam == null) {
            return DEFAULT_ALIAS;
        }
        String u = request.getParameter(this.sessionParam);
        if (u == null) {
            return DEFAULT_ALIAS;
        }
        if (!ALIAS_PATTERN.matcher(u).matches()) {
            return DEFAULT_ALIAS;
        }
        return u;
    }


    private String createSessionCookieValue(Map<String, String> sessionIds) {

        if (sessionIds.isEmpty()) {
            return "";
        }
        if (sessionIds.size() == 1 && sessionIds.keySet().contains(DEFAULT_ALIAS)) {
            return sessionIds.values().iterator().next();
        }

        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, String> entry : sessionIds.entrySet()) {
            String alias = entry.getKey();
            String id = entry.getValue();

            buffer.append(alias);
            buffer.append(" ");
            buffer.append(id);
            buffer.append(" ");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

    @SuppressWarnings("unchecked")
    private Set<String> getSessionIdsWritten(HttpServletRequest request) {
        Set<String> sessionsWritten = (Set<String>) request
                .getAttribute(SESSION_IDS_WRITTEN_ATTR);
        if (sessionsWritten == null) {
            sessionsWritten = new HashSet<String>();
            request.setAttribute(SESSION_IDS_WRITTEN_ATTR, sessionsWritten);
        }
        return sessionsWritten;
    }

}
