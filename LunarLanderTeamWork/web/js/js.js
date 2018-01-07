
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
var combustible = 100;
var gasolinaTotal = 100;
var intentos = 0;

var modeloNave = 1;
var modeloLuna = 1;
var dificultad = 1;

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
            if (n != "" && e != "" && j != "") {
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
            } else {
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



    document.getElementById("instrucciones").onclick = function () {
        mostrarInstrucciones();
    };
    document.getElementById("reinicia").onclick = function () {
        reiniciarJuego();
    };
    document.getElementById("reanudar").onclick = function () {
        reanudar();
    };
    document.getElementById("botonAjustes").onclick = function () {
        mostrarAjustes();
    };
    document.getElementById("jugarOtraVez").onclick = function () {
        reiniciarJuego();
    };

    document.getElementById("jugarAgain").onclick = function () {
        reiniciarJuego();
    };
    document.getElementById("pausa").onclick = function () {
        pausar();
    };
    document.getElementById("dificultad").onclick = function cambiarDificultad() {
        switch (dificultad) {
            case 1:
                combustible = 50;
                gasolinaTotal = 50;
                document.getElementById("dificultad").innerHTML = "Media";
                dificultad = 2;
                restart();
                break;
            case 2:
                combustible = 25;
                gasolinaTotal = 35;
                document.getElementById("dificultad").innerHTML = "Difícil";
                dificultad = 3;
                restart();
                break;
            case 3:
                combustible = 100;
                gasolinaTotal = 100;
                document.getElementById("dificultad").innerHTML = "Fácil";
                dificultad = 1;
                restart();
                break;
        }
    }

    //CAMBIAR LA IMAGEN DE LA LUNA
    document.getElementById("modeloLuna").onclick = function cambiarModeloLuna() {
        switch (modeloLuna) {
            case 1:
                document.getElementById("luna").src = "img/mod2luna.png";
                document.getElementById("modeloLuna").innerHTML = "Gris";
                modeloLuna = 2;
                break;
            case 2:
                document.getElementById("luna").src = "img/luna.png";
                document.getElementById("modeloLuna").innerHTML = "Amarilla";
                modeloLuna = 1;
                break;
        }
    }

    //CAMBIAR LA IMAGEN DE LA NAVE Y EL MOTOR
    document.getElementById("modeloNave").onclick = function cambiarModeloNave() {
        switch (modeloNave) {
            case 1:
                document.getElementById("imgNave").src = "img/mod2nave.gif";
                document.getElementById("imgMotor").src = "img/mod2motor.gif";
                document.getElementById("modeloNave").innerHTML = "Modelo PodRacer";
                modeloNave = 2;
                restart();
                break;
            case 2:
                document.getElementById("imgNave").src = "img/nave.png";
                document.getElementById("imgMotor").src = "img/motor.gif";
                document.getElementById("modeloNave").innerHTML = "Modelo Estándar";
                modeloNave = 1;
                restart();
                break;
        }
    }

    //mostrar menú móvil
    $("#shown").click(function () {
        document.getElementsByClassName("c")[0].style.display = "block";
        stop();
    });
    //ocultar menú móvil
    $("#hidem").click(function () {
        document.getElementsByClassName("c")[0].style.display = "none";
        start();
    });

    var botonOnSmartphone = document.getElementById("botonOn");
    botonOnSmartphone.addEventListener("touchstart", handlerFunction, false);
    botonOnSmartphone.addEventListener("touchend", endingFunction, false);
    function handlerFunction(event) {
        motorOn();
    }
    function endingFunction(event) {
        motorOff();
    }
    //encender/apagar el motor al hacer click en la pantalla
    window.onkeydown = function (e) {
        var claveTecla;
        if (window.event)
            claveTecla = window.event.keyCode;
        else if (e)
            claveTecla = e.which;
        if ((claveTecla == 32))
        {
            motorOn();
        }
    }
    window.onkeyup = motorOff;

    //Empezar a mover la nave justo después de cargar la página
    start();
    var botonOnSmartphone = document.getElementById("botonOn");
    botonOnSmartphone.addEventListener("touchstart", handlerFunction, false);
    botonOnSmartphone.addEventListener("touchend", endingFunction, false);
    function handlerFunction(event) {
        encenderMotor();
    }
    function endingFunction(event) {
        apagarMotor();
    }

    //CON TECLADO (tecla ESPACIO)
    window.onkeydown = function (e) {
        var claveTecla;
        if (window.event)
            claveTecla = window.event.keyCode;
        else if (e)
            claveTecla = e.which;
        if ((claveTecla == 32))
        {
            motorOn();
        }
    }
    window.onkeyup = motorOff();

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
        finalizarJuego();
    }
}
function motorOn() {
    //el motor da aceleración a la nave
    a = -g;
    document.getElementById("fuel").innerHTML = porcentajeGasolina();
    document.getElementById("imgMotor").style.display = "block";
    if (timerFuel == null) {
        timerFuel = setInterval(function () {
            actualizarFuel();
        }, 100);
    }
    if (combustible <= 0) {
        apagarMotor();
        document.getElementById("fuel").innerHTML = 0;
    }
}
function motorOff() {
    a = g;
    document.getElementById("imgMotor").style.display = "none";
    clearInterval(timerFuel);
    timerFuel = null;
}
function actualizarFuel() {
    combustible -= 0.1;
    document.getElementById("fuel").innerHTML = porcentajeGasolina();
    if (combustible <= 0) {
        motorOff();
        document.getElementById("fuel").innerHTML = 0;
    }
}

function porcentajeGasolina() {
    var result = combustible * 100 / gasolinaTotal;
    return result.toFixed(2);
}

function finalizarJuego() {
    if (v > 5) {
        eventosOff();
        document.getElementById("gameOver").style.display = "block";
        document.getElementById("intentos").innerHTML = intentos;

    } else {
        document.getElementById("userWin").style.display = "block";
        eventosOff();
    }
}
function eventosOff() {
    document.getElementById("izquierda").style.pointerEvents = 'none';
    document.getElementById("derecha").style.pointerEvents = 'none';
}
//HACER QUE LOS DIVS IZQUIERDA Y DERECHA SI RECIBAN EVENTOS ONCLICK
function eventosOn() {
    document.getElementById("izquierda").style.pointerEvents = 'auto';
    document.getElementById("derecha").style.pointerEvents = 'auto';
}
function mostrarInstrucciones() {
    pausar();
    eventosOff();
    document.getElementById("menuInstrucciones").style.display = "block";
}

function ocultarInstrucciones() {
    document.getElementById("menuInstrucciones").style.display = "none";
    eventosOn();
}

function pausar() {
    stop();
    document.getElementById("pausa").style.display = "none";
    document.getElementById("reanudar").style.display = "inline-block";
}

function reiniciarJuego() {
    stop();
    document.getElementById("reanudar").style.display = "none";
    document.getElementById("pausa").style.display = "inline-block";
    intentos++;
    y = 5;
    v = 0;
    g = 1.622;
    a = g;
    dt = 0.016683;
    combustible = gasolinaTotal;
    document.getElementById("fuel").innerHTML = porcentajeGasolina();
    reanudar();
    clearInterval(timer);
    start();
    eventosOn();
    document.getElementById("intentos").innerHTML = intentos;
    document.getElementById("gameOver").style.display = "none";
    document.getElementById("userWin").style.display = "none";

}

function reanudar() {
    stop();
    start();
    document.getElementById("reanudar").style.display = "none";
    document.getElementById("pausa").style.display = "inline-block";
}

function mostrarAjustes() {
    pausar();
    eventosOff();
    document.getElementById("settings").style.display = "block";
}
function ocultarAjustes() {
    document.getElementById("settings").style.display = "none";
    eventosOn();
}

function restart() {
    stop();
    y = 5;
    v = 0;
    g = 1.622;
    a = g;
    dt = 0.016683;
    combustible = gasolinaTotal;
    document.getElementById("fuel").innerHTML = porcentajeGasolina();
}

function porcentajeGasolina() {
    var result = combustible * 100 / gasolinaTotal;
    return result.toFixed(2);
}