package de.unidue.inf.is;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import de.unidue.inf.is.domain.Kurs;
import de.unidue.inf.is.stores.EinschreibenStore;
import de.unidue.inf.is.stores.KursStore;

public final class NewEnrollServlet extends HttpServlet {


    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String showTextfield;
        String kname;
        kname=request.getParameter("kname");
        String  kid;
        kid=(String) request.getParameter("kid");
        int kidAsint=Integer.parseInt(kid);
        // just to know if we need to shaw a text field for the password
        try(EinschreibenStore einschreibendb=new EinschreibenStore()) {

            if (einschreibendb.hasPassword(kidAsint)){ // hasPasswod --> true --> show text field for password
                showTextfield="block";

            }else{   // hasPasswod --> false --> don't show text field for password
                showTextfield="none";
            }
            request.setAttribute("showTextfield",showTextfield);
        }

            request.setAttribute("kid",kid);
            request.setAttribute("kname",kname);


        request.getRequestDispatcher("/new_enroll.ftl").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String  kid;
        kid=request.getParameter("kid");

        String usr_einschreibeschluessel=request.getParameter("password");


        int kidAsint=Integer.parseInt(kid);
        int bnummer=SignedInUser.logedin.getBnummer();


        Kurs kurs;

        try(KursStore kursdb =new KursStore();
            EinschreibenStore einschreibendb=new EinschreibenStore())
        {
            // getting the freieplaetze
            kurs=kursdb.getKursInformation(kidAsint);
            int freiePlaetze=kurs.getFreiePlaetze();
            String kname=kurs.getName();


           // if not authenticated --> wrong password
            if (! einschreibendb.authenticated(kidAsint,usr_einschreibeschluessel)) {
                // redirect to the same page to try again
                // not forget to show the password text field
                // and kname and kid
                request.setAttribute("showTextfield","block");
                request.setAttribute("kname",kname);
                request.setAttribute("kid",kid);
                request.getRequestDispatcher("/new_enroll.ftl").forward(request, response);
            }else
            {

            // check if not Enrolled  (just for security reasons :)
                // freieplaetze is available
            if (freiePlaetze > 0 &&
                    (!einschreibendb.isEnrolled(bnummer, kidAsint))) {

                einschreibendb.einschreiben(bnummer, kidAsint);
                String massage = "";
                massage = "<h1> Sie sind in dem Kurs eingeschrieben <h1>";
                request.setAttribute("feedback", massage);

                request.getRequestDispatcher("/feedback_msg.ftl").forward(request, response);

            } else {
                String massage2 = "<h3>OPS leider es gibt keine Freieplätze </h3>";
                request.setAttribute("message", massage2);
                request.getRequestDispatcher("/error_msg.ftl").forward(request, response);
            }
        }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
