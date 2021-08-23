package de.unidue.inf.is;
import de.unidue.inf.is.stores.KursStore;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public final class NewCourseServlet extends HttpServlet {

        private static final long serialVersionUID = 1L;

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            //showing the submit form
            request.getRequestDispatcher("/new_course.ftl").forward(request, response);
        }


        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                IOException {

            String name =request.getParameter("name");
            String einschreibeschluessel = request.getParameter("einschreinschluessel");
            String beschreibungstext= request.getParameter("beschreibungstext");
            String freieplaetze=request.getParameter("freieplaetze");


            String fehlermeldung ="";

            if (einschreibeschluessel.length() >50)
                fehlermeldung +="<p>einschreibschlüssel darf nicht mehr als 50 Zeichen sein </p>" ;

            // testing if freieplaetze not added from the user !
            if (freieplaetze.equals("")) {
                fehlermeldung += "<p>freieplaetze darf nicht leer sein!</p><br>";
            }else{
                int freieplaetzeAsint =Integer.parseInt(freieplaetze);
                if(freieplaetzeAsint < 1 || freieplaetzeAsint > 100){
                    fehlermeldung+="<p>freieplaetze soll ziwischen 1 und 100 !</p><br>";
                }
            }
            //testing on beschreibungstext
            if(beschreibungstext.equals("")){
                fehlermeldung+= "<p>beschreibungstext darf nicht leer sein!</p><br>";
            }
            //testing on name
            if(name.length()==0 || name.length() >50){
                fehlermeldung+="<p>name soll nicht leer sein oder größer als 50 Zeichen !</p><br>";

            }
            // if we have any errors then forward to error_msg with fehlermeldung
            if (! fehlermeldung.equals("")){
                request.setAttribute("message",fehlermeldung);
                request.getRequestDispatcher("/error_msg.ftl").forward(request,response);

                // we have no errors so we can insert the new course
            }else {
                int freieplaetzeAsInt=Integer.parseInt(freieplaetze);
                try(KursStore kursdb= new KursStore()){
                    kursdb.createNewKurs(SignedInUser.logedin,name,einschreibeschluessel,freieplaetzeAsInt,beschreibungstext);
                    String message="<p>der Kurs wurde erfolgreich erstellt</p>";
                    request.setAttribute("feedback",message);
                    request.getRequestDispatcher("/feedback_msg.ftl").forward(request,response);
                }

            }
        }

}
