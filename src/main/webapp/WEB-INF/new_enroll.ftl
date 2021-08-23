<html>
<style>
    a {
        text-decoration: none;
        position: absolute;
        right: 0;
        text-align: right;
        color: #fff;
    }
    a:hover {
        text-decoration: underline;
    }

    .container {
        position: fixed;
        top: 50%;
        left: 50%;

        width: 55%;
        padding: 30px 0;

        transform: translate(-50%, -50%);
        text-align: center;

        color: #050000;
        background: rgb(184, 232, 241);
    }

    #signin{
        display: block;
        width: 35%;
    }

    #textfield{
        display: ${showTextfield};
        width: 50%;
        position: fixed;
        top: 50%;
        left: 25%;
    }
    body{
        text-align:center;
        background: #efe4bf none repeat scroll 0 0;
    }
    h1{
        color: #fff;
        background-color: #0fbfee;
        width: -moz-available;
        padding: 1em 0em 1em 1em;

    }
</style>
<head>
    <title>Kurs einschreiben</title>
</head>
<body>
<h1>Kurs einschreiben</h1>

<div class="container" id="signin">
    <h2>${kname}</h2>
    <form  action="/new_enroll?kid=${kid}" method="post">
        <input type="text" name="password" placeholder="Password" id="textfield">
        <br />
        <input class="button" type="submit" name="commit" value="einschreiben" style="width: 150px;height:50px" />
    </form>
</div>

<form name="Hauptseite" action="/view_main" method="get">
    <input type="submit" value="Zur Haupseite" style="width: 150px;height:50px" />
</form>

</body>
</html>