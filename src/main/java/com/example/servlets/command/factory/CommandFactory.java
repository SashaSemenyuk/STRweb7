package com.example.servlets.command.factory;

import com.example.servlets.command.Command;
import com.example.servlets.command.authorihation.LoginCommand;
import com.example.servlets.command.authorihation.RegisterNewUserCommand;
import com.example.servlets.command.grouppersons.AddNewPersonCommand;
import com.example.servlets.command.grouppersons.WelcomeCommand;
import com.example.servlets.command.LoginPageCommand;
import com.example.servlets.command.RegisterPageCommand;
import com.example.servlets.command.authorihation.SignOutCommand;

public class CommandFactory {
    public static Command create(String command) {
        command = command.toUpperCase();
        System.out.println(command);
        CommandType commandEnum = CommandType.valueOf(command);
        Command resultCommand;
        switch (commandEnum) {
            case LOGIN: {
                resultCommand = new LoginCommand();
                break;
            }
            case REGISTER_NEW_USER: {
                resultCommand = new RegisterNewUserCommand();
                break;
            }
            case SIGN_OUT: {
                resultCommand = new SignOutCommand();
                break;
            }
            case ADD_NEW_PERSON: {
                resultCommand = new AddNewPersonCommand();
                break;
            }
            case LOGIN_PAGE: {
                resultCommand = new LoginPageCommand();
                break;
            }
            case WELCOME: {
                resultCommand = new WelcomeCommand();
                break;
            }
            case REGISTRATION_PAGE: {
                resultCommand = new RegisterPageCommand();
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid command" + commandEnum);
            }
        }
        return resultCommand;
    }
}
