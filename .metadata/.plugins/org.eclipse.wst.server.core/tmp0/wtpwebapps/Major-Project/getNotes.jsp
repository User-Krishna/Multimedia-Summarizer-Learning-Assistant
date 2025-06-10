<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%
    ArrayList<String> notes = (ArrayList<String>) session.getAttribute("notes");

    if (notes != null && !notes.isEmpty()) {
        for (int i = 0; i < notes.size(); i++) {
            String note = notes.get(i);
%>
    <!-- Odd Notes (Pink) -->
    <%
        if (i % 2 == 0) { // For odd index, pink
    %>
    <div class="paper pink" id="note-<%= i %>">
        <div class="tape-section"></div>
        <p><%= note %></p>
        <div class="tape-section"></div>
        <button class="download-btn" onclick="downloadTape('note-<%= i %>')">Download</button>
    </div>
    <%
        } else { // For even index, blue
    %>
    <!-- Even Notes (Blue) -->
    <div class="paper blue" id="note-<%= i %>">
        <div class="top-tape"></div>
        <p><%= note %></p>
        <button class="download-btn" onclick="downloadTape('note-<%= i %>')">Download</button>
    </div>
    <%
        }
    %>
<%
        }
    } else {
%>
<p>No notes available.</p>
<%
    }
%>
