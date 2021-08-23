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
<head><title>Hauptseite</title></head>

<body>
    <h1> OnlineLearner Website</h1>

    <h3>Sie sind als ${eingelogt} eingelogt</h3>

  <h2>Meine Kurse</h2>

  <table class="centerBlock">
      <tr>
          <th>Kursname</th>
          <th>Ersteller</th>
          <th>Freie Plätze</th>
      </tr>
    <#list mkurse as kurs>
      <tr>
        <td><a href="/view_my_course?kursid=${kurs.kid}" type="submit"  class="btn-link">${kurs.name}</a></td>
        <td>${kurs.erstellername}</td>
        <td>${kurs.freiePlaetze}</td>
      </tr>
    </#list>
  </table>
  <br>
  <br>
  <h2>verfügbare Kurse</h2>
  <table class="centerBlock">
      <tr>
          <th>Kursname</th>
          <th>Ersteller</th>
          <th>Freie Plätze</th>
      </tr>
    <#list vkurse as kurs>
      <tr>
        <td><a href="/view_available_course?kursid=${kurs.kid}" type="submit" class="btn-link">${kurs.name}</a></td>
         <td>${kurs.erstellername}</td>
        <td>${kurs.freiePlaetze}</td>
      </tr>
    </#list>
  </table>

          <form name="kurserstellen" action="/new_course" method="get" class="left" >
              <input type="submit" value="Kurs erstellen" style="width: 200px;height:50px"/>
          </form>
</body>
</html>