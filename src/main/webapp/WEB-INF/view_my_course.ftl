<html>
<style>
    h3{
        color: #454547;
        text-align: left;
    }
    table, th, td {
        border: 1px solid #0fbfee;
    }
    * {
        margin:0;
        padding:0;
    }

    body{
        text-align:center;
        background: #efe4bf none repeat scroll 0 0;
    }
    .left{
        text-align: left;
        background: #efe4bf none repeat scroll 0 0;
    }

    h1{
        color: #fff;
        background-color: #0fbfee;
        width: -moz-available;
        padding: 1em 0em 1em 1em;

    }
    .centerBlock{
        margin:0 auto;
    }
</style>
<head><title>Kurs Details</title></head>
<body>

      <h1> Informationen </h1>

      <table class="centerBlock">
          <tr>
     <th><h2> ${kursinfo1.name}</h2></th>
              </tr>
          <tr>
     <th><h2> ${kursinfo1.erstellername}</h2></th>
              </tr>
          <tr>
     <th><h2> ${kursinfo1.beschreibungstext}</h2></th>
          </tr>
      </table>

      <form name="Löschen" action="/view_my_course?kid=${kid}" method="post" class="left" >
          <input type="submit" value="Kurs Löschen" style="width: 100px;height:25px"/>
      </form>

        <br> <br>



        <br> <br>

      <form name="bewerten" action="/new_assess" method="get" class="left">
          <input type="hidden" name="kid" id="kid" value="${kursinfo1.kid}" />
          <input type="submit" value="eine Abgabe zufällig Bewerten" style="width:200px;height:25px" />
      </form>


      <br><br>

      <h2>Liste der Aufgaben  </h2>
      <table class="centerBlock">
          <tr>
              <th>Aufgabe</th>
              <th>Meine Abgabe</th>
              <th>Bewertungsnote</th>

          </tr>
      <#list meineAufgaben as aufgabe>
          <tr>
              <td><a href="/new_assignment?kid=${aufgabe.kid}&anummer=${aufgabe.anummer}" type="submit"  class="btn-link">${aufgabe.name}</a></td>
              <td>${aufgabe.abgabe.abgabe_text}</td>
              <td>${aufgabe.abgabe.note}</td>
          </tr>

      </#list>
      </table>



      <form name="Zur_Haupseite" action="/view_main" method="get" class="left">
          <input type="submit" value="zurück" style="width: 100px;height:25px" />
      </form>
</body>
</html>