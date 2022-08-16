package com.example.WebAppProject.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Redirects users to customized error pages in case these errors occur.
 *
 * @author Daryna Frolova
 */

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger logger = LogManager.getLogger(CustomErrorController.class);

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        String errorPage = "error/error";

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                logger.error("ERROR 404: User with id {} tried to access a page that does not exist",
                        ContextController.getUser().getId());
                errorPage = "error/404";

            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                logger.error("ERROR 403: User with id {} tried to access a page without permission to view it",
                        ContextController.getUser().getId());
                errorPage = "error/403";

            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                logger.error("ERROR 500: User with id {} tried to access a page, but the server could not complete the request",
                        ContextController.getUser().getId());
                errorPage = "error/500";

            }
        }

        model.addAttribute("user", ContextController.getUser());
        return errorPage;
    }
}
