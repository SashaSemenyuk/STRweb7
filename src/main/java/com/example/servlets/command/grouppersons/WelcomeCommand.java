package com.example.servlets.command.grouppersons;
import com.example.servlets.exception.IncorrectDataException;
import com.example.servlets.exception.ServiceException;
import com.example.servlets.model.Person;
import com.example.servlets.service.PersonService;
import com.example.servlets.utils.page.Page;
import com.example.servlets.command.Command;
import com.example.servlets.command.CommandResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.example.servlets.command.grouppersons.constant.GroupConstant.*;

import java.util.List;

public class WelcomeCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, IncorrectDataException {
        PersonService personService = new PersonService();
        List<Person> clients = personService.findAll();
        if (!clients.isEmpty()) {
            request.setAttribute(LISTGROUP, clients);
        }
        return new CommandResult(Page.WELCOME_PAGE.getPage(), false);
    }
}
