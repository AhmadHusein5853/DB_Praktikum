<html>
<style>
    h3{
        color: red;
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
<head><title>error_msg</title></head>

<body>
<h1>falsche Eingaben</h1>

<div class="error"><h3>${message}</h3></div>


<form name="Hauptseite" action="/view_main" method="get">
    <input type="submit" value="Zur Haupseite" style="width: 200px;height:50px" />
</form>


</body>
</html>