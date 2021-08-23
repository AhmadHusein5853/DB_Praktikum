package de.unidue.inf.is;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.stores.KursStore;


/**
 * Einfaches Beispiel, das die Verwendung der Template-Engine zeigt.
 */
public final class ViewMainServlet extends HttpServlet {


    private static final long serialVersionUID = 1L; //we  should know why we are using it

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List mkusre;
        List vkurse;

        try(KursStore kursdb=new KursStore()){
            mkusre=kursdb.getMkurse(SignedInUser.logedin);
            vkurse=kursdb.getVkurse(SignedInUser.logedin);

        }
        // to show the name of the loged user
        request.setAttribute("eingelogt",SignedInUser.logedin.getName());
        // adding the lists to the request
        request.setAttribute("mkurse", mkusre);
        request.setAttribute("vkurse", vkurse);
        request.getRequestDispatcher("/view_main.ftl").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException {

        doGet(request, response);
    }
}
