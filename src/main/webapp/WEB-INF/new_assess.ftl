<!DOCTYPE html>
<html lang="en">
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
<head>
    <meta charset="UTF-8">
    <title>Abgabe bewerten</title>
</head>

<h1>Abgabe bewerten </h1>
<h2>Aufgabe: ${Aufgabename}</h2>
<br>

<h2>Beschreibung: ${beschreibung}</h2>
<br>
<h2>Abgabetext: ${abgabetext}</h2>
<br>
<h2>Bewertungsnote:</h2>
<br>
<br>
    <form action="/new_assess?kid=${kid}&aid=${aid}" method="post">


        <input type="radio" id="1" name="note" value="1" checked>
        <label for="ar">1</label><br>
        <input type="radio" id="2" name="note" value="2">
        <label for="en">2</label><br>
        <input type="radio" id="3" name="note" value="3">
        <label for="ar">3</label><br>
        <input type="radio" id="4" name="note" value="4">
        <label for="en">4</label><br>
        <input type="radio" id="5" name="note" value="5">
        <label for="en">5</label><br>
        <h2> Kommentar:</h2>

        <textarea id="kommentar" name="kommentar" rows="6" cols="35" style="top: 50%;left: 40%;right: 40%;width: 30%"></textarea>


        <input type="submit" value="Bewerten" style="width: 100px;height:25px">
    </form>

</body>
</html>