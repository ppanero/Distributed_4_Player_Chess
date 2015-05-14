/*
 *  Created by raj.srivastava on 24/03/15
 */

package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class URLController
{
    @RequestMapping("/login")
    public String login()
    {
        return "login";
    }

    @RequestMapping("/join")
    public String join()
    {
        return "join";
    }

    @RequestMapping("/game")
    public String game()
    {
        return "game";
    }

    @RequestMapping("/profile")
    public String profile()
    {
        return "profile";
    }
}
