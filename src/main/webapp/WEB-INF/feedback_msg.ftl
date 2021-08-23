<html>
<style>
    h3{
        color: green;
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

    .centerBlock{
        margin:0 auto;
    }
</style>
<head><title>feedback_msg</title></head>

<body>

<div class="feedback"><h3>${feedback}</h3></div>

<form name="Hauptseite" action="/view_main" method="get">
    <input type="submit" value="Zur Haupseite" style="width: 100px;height:25px"/>
</form>


</body>
</html>