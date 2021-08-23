<!DOCTYPE html>
<html lang="en">
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
<head>
    <meta charset="UTF-8">
    <title>Abgabe einreichen</title>
    <h1>Abgabe einreichen</h1>
    <table class="centerBlock">
        <tr>
            <th>
    <h2> Kurs:</h2>
            </th>
            <th>
        <h2>${kursinfo.name}</h2>
            </th>
        </tr>
        <tr>
            <th>
        <h2> Aufgabe:</h2>
            </th>
            <th>
        <h2>${aufgabe.name}</h2>
            </th>
        <tr>
            <th>
    <h2> Beschreibung: </h2>
            </th>
            <th>
    <h2>${aufgabe.beschreibung}</h2>
            </th>
    </table>
    <br>
    <br>

    <form action="/new_assignment?kid=${aufgabe.kid}&anummer=${aufgabe.anummer}" method="post">
        <h2><label for="abgabetext">Abgabetext:</label></h2>
        <br>
        <textarea id="abgabetext" name="abgabetext" rows="7" cols="50"></textarea><br>
        <input type="submit" value="einreichen" style="width: 100px;height:25px">
    </form>
</head>
<body>

</body>
</html>