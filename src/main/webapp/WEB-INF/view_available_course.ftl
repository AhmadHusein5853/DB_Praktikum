<html>
<style>
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
            <th>
    <h2> ${kursinfo.name}</h2>
            </th>
        </tr>
        <tr>
            <th>
    <h2> ${kursinfo.erstellername}</h2>
            </th>
        </tr>
        <tr>
            <th>
    <h2> ${kursinfo.beschreibungstext}</h2>
            </th>
        </tr>
    </table>

    <a href="/new_enroll?kid=${kursinfo.kid}&kname=${kursinfo.name}" type="submit"  class="btn-link">
        <input type="submit" value="einschreiben" style="width: 100px;height:25px" />
    </a>    <br> <br>


    <form name="kurserstellen" action="/view_main" method="get">
        <input type="submit" value="zurück" style="width: 100px;height:25px"  />
    </form>

    </body>
</html>