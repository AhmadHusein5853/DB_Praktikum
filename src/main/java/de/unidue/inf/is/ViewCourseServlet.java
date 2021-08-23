package de.unidue.inf.is;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import de.unidue.inf.is.domain.Aufgaben;
import de.unidue.inf.is.domain.Kurs;
import de.unidue.inf.is.stores.AufgabenStore;
import de.unidue.inf.is.stores.EinschreibenStore;
import de.unidue.inf.is.stores.KursStore;
import de.unidue.inf.is.stores.StoreException;


public final class ViewCourseServlet extends HttpServlet {


    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String kursid = request.getParameter("kursid");
        int kursIdAsInt = Integer.parseInt(kursid);
        int bnummer = SignedInUser.logedin.getBnummer();
        try (EinschreibenStore einschreibendb = new EinschreibenStore()) {

            // testing if the user with his bnummer in the course is not enrolled
            // then redirect hem to the page /view_available_course

            if (!einschreibendb.isEnrolled(bnummer, kursIdAsInt)) {
                Kurs kursinfo;
                try (KursStore kursdb = new KursStore()) {
                    kursinfo = kursdb.getKursInformation(kursIdAsInt);

                }
                request.setAttribute("kursinfo", kursinfo);
                request.getRequestDispatcher("/view_available_course.ftl").forward(request, response);
            } else {
                // the user is Enrolled so we redirect hem to  /view_my_course
                Kurs kursinfo2;
                List<Aufgaben> meineAufgaben;

                try (KursStore kursdb = new KursStore();
                     AufgabenStore aufgabedb = new AufgabenStore()) {

                    kursinfo2 = kursdb.getKursInformation(kursIdAsInt);
                    meineAufgaben = aufgabedb.getAufgaben(kursIdAsInt);
                }
                request.setAttribute("kursinfo1", kursinfo2);
                request.setAttribute("meineAufgaben", meineAufgaben);

                request.setAttribute("kid", kursid);

                request.getRequestDispatcher("/view_my_course.ftl").forward(request, response);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String kursid = request.getParameter("kid");
        int kursIdAsInt = Integer.parseInt(kursid);
        Kurs kurs;
        try (KursStore kursdb = new KursStore()) {
            kurs = kursdb.getKursInformation(kursIdAsInt);
            int erstellerid = kurs.getErstellerid();

            // to make sure the user is the ersteller --> he is allowed to delete the course
            if (erstellerid == SignedInUser.logedin.getBnummer()) {
                kursdb.deleteCourse(kursIdAsInt);
                String message = "<p>der Kurs wurde erfolgreich gelöscht</p>";
                request.setAttribute("feedback", message);
                request.getRequestDispatcher("/feedback_msg.ftl").forward(request, response);

            } else {
                String feedback = "Sie dürfen diesen Kurs nicht löschen , denn Sie sind nicht der Ersteller";
                request.setAttribute("message", feedback);
                request.getRequestDispatcher("/error_msg.ftl").forward(request, response);
            }

        } catch (Exception e) {
            throw new StoreException(e);
        }

    }
}
