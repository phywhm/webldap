package phy.test.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by phy on 2017/4/26.
 */
@Controller
@RequestMapping(value = "html")
public class LogoutController {

//    @RequestMapping("logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        request.setAttribute("msg","退出成功");
//        request.getSession().invalidate();
//        return "index";
//    }
}
