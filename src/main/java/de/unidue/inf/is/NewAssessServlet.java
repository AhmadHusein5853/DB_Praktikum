package de.unidue.inf.is;

import de.unidue.inf.is.domain.Korrektur;
import de.unidue.inf.is.stores.EinschreibenStore;
import de.unidue.inf.is.stores.KorrekturStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class NewAssessServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String kid = request.getParameter("kid");
        int kidAsInt = Integer.parseInt(kid);
        try (KorrekturStore korrekturdb = new KorrekturStore()) {
            // to get all "abgaben"  that not submitted by the actual user
            ArrayList<Korrektur> abgaben = korrekturdb.getAlleAbgaben(kidAsInt, SignedInUser.logedin.getBnummer());
            // to get the "abgaben" that has been noted form the actual user
            ArrayList<Korrektur> bewerten = korrekturdb.getMeineKorrekture(SignedInUser.logedin.getBnummer());

            ArrayList<Korrektur> elementsToRemove = new ArrayList<>();
            // filtering the aid's that this user corrected before
            for (Korrektur B : bewerten) {
                for (Korrektur k : abgaben) {
                    if (B.getAid() == k.getAid()) {
                        elementsToRemove.add(k);
                    }
                }
            }

            abgaben.removeAll(elementsToRemove);


            // if there is no submissions in this course
            if (abgaben.size() == 0) {

                request.setAttribute("feedback", "Es gibt keine Abgaben in diesem Kurs zu korrigieren !");
                request.getRequestDispatcher("/feedback_msg.ftl").forward(request, response);

                // there is submission in this course
            } else {

                int abgabenAnzahl = abgaben.size();

                // random number between 0 and length of abgaben
                int randomNum = ThreadLocalRandom.current().nextInt(0, abgabenAnzahl);

                Korrektur randomSubmition = abgaben.get(randomNum);

                request.setAttribute("Aufgabename", randomSubmition.getName());
                request.setAttribute("beschreibung", randomSubmition.getBeschreibung());
                request.setAttribute("abgabetext", randomSubmition.getAbgabe_text());
                request.setAttribute("aid", randomSubmition.getAid());


                request.setAttribute("kid", kid);

                request.getRequestDispatcher("/new_assess.ftl").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String kid = request.getParameter("kid");
        int kidAsint = Integer.parseInt(kid);
        String kommentar = request.getParameter("kommentar");
        String aid = request.getParameter("aid");
        int aid2 = Integer.parseInt(aid);


        try (EinschreibenStore einschreibendb = new EinschreibenStore();
             KorrekturStore korrekturdb = new KorrekturStore()) {

            ArrayList<Korrektur> bewerten2 = korrekturdb.getMeineKorrekture(SignedInUser.logedin.getBnummer());

            String noteAsString = request.getParameter("note");
            int note = Integer.parseInt(noteAsString);

            // testing if the user isn't enrolled , or the note isn't between (1,5)
            if (!einschreibendb.isEnrolled(SignedInUser.logedin.getBnummer(), kidAsint) || note < 1 || note > 5) {
                String massage2 = "<h3>OPS leider es gibt ein Proplem , Sie sind entweder in diesem kurs nicht eingeschrieben , oder falsche note eingetragen </h3>";
                request.setAttribute("message", massage2);
                request.getRequestDispatcher("/error_msg.ftl").forward(request, response);

            } else { // testing if the chosen aid isn't corrected from this user
                for (Korrektur B : bewerten2) {
                    if (B.getAid() == aid2) {
                        String massage2 = "<h3>OPS leider Sie haben diese Abgabe vorher schon  bewertet :) :)  </h3>";
                        request.setAttribute("message", massage2);
                        request.getRequestDispatcher("/error_msg.ftl").forward(request, response);

                        return;

                    }
                }

                korrekturdb.insertRating(SignedInUser.logedin.getBnummer(), aid2, note, kommentar);
                korrekturdb.complete();

                response.sendRedirect("/view_main");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


