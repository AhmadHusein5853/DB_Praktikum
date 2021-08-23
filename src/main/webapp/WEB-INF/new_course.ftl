<html>
<head>
    <title>Kurs erstellen</title>
</head>
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
<body>
<h1> Kurs erstellen </h1>

<form action="/new_course" method="post" >
    <h2><label for="name">Name:</label></h2><br>
    <input type="text" id="name" name="name" placeholder="Kusrname" ><br><br>
    <h2><label for="einschreinschluessel">Einschreibschlüssel:</label><h2><br>
    <input type="text" id="einschreinschluessel" name="einschreinschluessel"><br><br>

    <h2><label for="freieplaetze">freieplätze:</label></h2><br>
    <input type="number" id="freieplaetze" name="freieplaetze"  min="1" max="100" ><br><br><br>

    <h2><label for="beschreibungstext">beschreibungstext:</label></h2><br>
    <textarea id="beschreibungstext" name="beschreibungstext" rows="4" cols="50"></textarea><br><br>
    <input type="submit" value="Submit" style="width: 100px;height:25px">
</form>
<br>
<br>



<form name="kurserstellen" action="/view_main" method="get">
    <input type="submit" value="zurück" style="width: 100px;height:25px" />
</form>


</body>
</html>