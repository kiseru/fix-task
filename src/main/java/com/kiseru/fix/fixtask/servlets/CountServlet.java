package com.kiseru.fix.fixtask.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiseru.fix.fixtask.dto.MovesDto;
import com.kiseru.fix.fixtask.services.HorseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
@WebServlet(urlPatterns = CountServlet.URI)
public class CountServlet extends HttpServlet {

    static final String URI = "/hourse/servlet/count";

    private final HorseService horseService;

    @Autowired
    public CountServlet(HorseService horseService) {
        this.horseService = horseService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var writer = resp.getWriter();
        try {
            var fieldHeight = Integer.parseInt(req.getParameter("height"));
            var fieldWidth = Integer.parseInt(req.getParameter("width"));
            var startPosition = Objects.requireNonNull(req.getParameter("start"));
            var endPosition = Objects.requireNonNull(req.getParameter("end"));
            var moves = horseService.getMovesCount(fieldHeight, fieldWidth, startPosition, endPosition);
            var movesDto = new MovesDto(moves);

            resp.setHeader("Content-Type", "application/json");
            var mapper = new ObjectMapper();
            writer.print(mapper.writeValueAsString(movesDto));
        } catch (NumberFormatException | NullPointerException e) {
            e.printStackTrace();
            var response = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid parameters");
            resp.setStatus(response.getStatus().value());
            resp.setHeader("Content-Type", "text/plain");
            writer.print(response.getMessage());
        } catch (ResponseStatusException e) {
            resp.setStatus(e.getStatus().value());
            resp.setHeader("Content-Type", "text/plain");
            writer.print(e.getMessage());
        }
        writer.flush();
    }
}
