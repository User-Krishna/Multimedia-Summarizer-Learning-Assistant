<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%
    String note = request.getParameter("note");
    if (note != null && !note.trim().isEmpty()) {
        // Use the implicit session object
        ArrayList<String> notes = (ArrayList<String>) session.getAttribute("notes");
        if (notes == null) {
            notes = new ArrayList<>();
        }
        notes.add(note);
        session.setAttribute("notes", notes);

        // Return the updated list of notes
        for (String savedNote : notes) {
            out.print("<div>" + savedNote + "</div>");
        }
    } else {
        out.print("<div>No notes available.</div>");
    }
%>