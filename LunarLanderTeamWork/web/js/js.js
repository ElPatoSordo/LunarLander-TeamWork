
//ENTORNO
var g = 1.622;
var dt = 0.016683;
var timer = null;
var timerFuel = null;
//NAVE
var y = 10; // altura inicial y0=10%, debe leerse al iniciar si queremos que tenga alturas diferentes dependiendo del dispositivo
var v = 0;
var c = 100;
var a = g; //la aceleración cambia cuando se enciende el motor de a=g a a=-g (simplificado)
//MARCADORES
var velocidad = null;
var altura = null;
var combustible = null;

//al cargar por completo la página...
$(function () {

    velocidad = document.getElementById("velocidad");
    altura = document.getElementById("altura");
    combustible = document.getElementById("fuel");


    //definición de eventos
    //
    //
    
    $(function () { //onload...
    $("#submit").click(function () { //onclick event

        var url = "Registry";
        var emess = "Error desconocido";
        var n = $("#usern").val(); //get name from input
        var e = $("#pass1").val(); //get age from input
        var j = $("#name").val();
        if(n!=""&&e!=""&&j!=""){
        $.ajax({
            method: "POST",
            url: url,
            data: {user: n, password: e, nombre: j},
            success: function (u) {
                alert(u["mess"]);
            },
            error: function (e) {
                if (e["responseJSON"] === undefined)
                    alert(emess);
                else
                    alert(e["responseJSON"]["error"]);
            }
        });
        }
        else{
            alert("Completa todos los campos");
        }
    });
    
    $("#entrar").click(function () { //onclick event

        var url = "GetCookies";
        var emess = "Error desconocido";
        var n = $("#username").val(); //get name from input
        var e = $("#password").val(); //get age from input
        $.ajax({
            method: "POST",
            url: url,
            data: {user: n, password: e},
            success: function (u) {
                alert(u["mess"]);
                location.reload();
            },
            error: function (e) {
                if (e["responseJSON"] === undefined)
                    alert(emess);
                else
                    alert(e["responseJSON"]["error"]);
            }
        });

    });
});
    
    //mostrar menú móvil
    $("#shown").click(function() {
        document.getElementsByClassName("c")[0].style.display = "block";
        stop();
    });
    //ocultar menú móvil
    $( "#hidem" ).click(function() {
        document.getElementsByClassName("c")[0].style.display = "none";
        start();
    });
    //encender/apagar el motor al hacer click en la pantalla
    document.onclick = function () {
        if (a == g) {
            motorOn();
        } else {
            motorOff();
        }
    }
    //encender/apagar al apretar/soltar una tecla
    document.onkeydown = motorOn;
    document.onkeyup = motorOff;

    //Empezar a mover la nave justo después de cargar la página
    start();
});

//Definición de funciones
function start() {
    //cada intervalo de tiempo mueve la nave
    timer = setInterval(function () {
        moverNave();
    }, dt * 1000);
}

function stop() {
    clearInterval(timer);
}

function moverNave() {
    //cambiar velocidad y posicion
    v += a * dt;
    y += v * dt;
    //actualizar marcadores
    velocidad.innerHTML = v.toFixed(2);
    altura.innerHTML = y.toFixed(2);

    //mover hasta que top sea un 70% de la pantalla
    if (y < 70) {
        document.getElementById("nave").style.top = y + "%";
    } else {
        stop();
    }
}
function motorOn() {
    //el motor da aceleración a la nave
    a = -g;
    //mientras el motor esté activado gasta combustible
    if (timerFuel == null)
        timerFuel = setInterval(function () {
            actualizarFuel();
        }, 10);
}
function motorOff() {
    a = g;
    clearInterval(timerFuel);
    timerFuel = null;
}
function actualizarFuel() {
    //Restamos combustible hasta que se agota
    c -= 0.1;
    if (c < 0)
        c = 0;
    combustible.innerHTML = c.toFixed(2);
}
