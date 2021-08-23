package de.unidue.inf.is;
import de.unidue.inf.is.domain.Aufgaben;
import de.unidue.inf.is.domain.Kurs;
import de.unidue.inf.is.stores.AufgabenStore;
import de.unidue.inf.is.stores.KursStore;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class NewAssignmentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String kid=request.getParameter("kid");
        int kidAsInt = Integer.parseInt(kid);
        String anummer=request.getParameter("anummer");
        int anummerAsInt = Integer.parseInt(anummer);

        Aufgaben aufgabe;
        Kurs kursinfo;
        try(AufgabenStore aufgabendb =new AufgabenStore();
            KursStore kursdb =new KursStore()){
            aufgabe=aufgabendb.getAufgabe(kidAsInt,anummerAsInt);
            // just to bring the course name
            kursinfo=kursdb.getKursInformation(kidAsInt);
        }
        request.setAttribute("aufgabe",aufgabe);
        request.setAttribute("kursinfo",kursinfo);
        request.getRequestDispatcher("/new_assignment.ftl").forward(request,response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String kid=request.getParameter("kid");
        int kidAsInt=Integer.parseInt(kid);

        String anummer=request.getParameter("anummer");
        int anummerAsInt=Integer.parseInt(anummer);

        String abgabetext=request.getParameter("abgabetext");


        try(AufgabenStore aufgabendb =new AufgabenStore()){
            // if the user has submitted before --> false
           if(! aufgabendb.tryToSubmit(kidAsInt,SignedInUser.logedin.getBnummer(), anummerAsInt,abgabetext)){

               String message = "Sie haben schon für diese Aufgabe eine Lösung abgegeben";
               request.setAttribute("message",message);
               request.getRequestDispatcher("/error_msg.ftl").forward(request,response);

           }else{ // the submission has been added , and user will be redirected to the details kurs

                 response.sendRedirect("/view_my_course?kursid="+kid);
           }
        }
    }
}
