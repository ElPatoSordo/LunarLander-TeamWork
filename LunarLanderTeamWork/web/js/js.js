var g = 1.622;
var dt = 0.016683;
var timer = null;
var timerFuel = null;
//NAVE
var y = 10; // altura inicial y0=10%, debe leerse al iniciar si queremos que tenga alturas diferentes dependiendo del dispositivo
var v = 0;
var a = g; //la aceleración cambia cuando se enciende el motor de a=g a a=-g (simplificado)
//MARCADORES
var velocidad = null;
var altura = null;
var combustible = 100;
var gasolinaTotal = combustible;
var intentos = 0;

var modeloNave = 1;
var modeloLuna = 1;
var dificultad = 1;

var date = new Date();
var inittime;
var endtime;
var viciados = [];
var scores = [];
var online = [];
var configuraciones = [];


//al cargar por completo la página...
$(function () {

    velocidad = document.getElementById("velocidad");
    altura = document.getElementById("altura");
    combustible = document.getElementById("fuel");

    
    

//    var emess = "Error desconocido";
//    $.ajax({
//        type: "get",
//        url: "GetPost", //canviar al Servlet després de comprovar que funciona.
//        dataType: "json",
//        success: function (jsn) {
//            $.each(jsn, function (index, value) {
//                var obj = new Object();
//                //var ident = value.nombre;
//                //obj.id = ident;
//                obj.username = value.username;
//                obj.games = value.games;
//                viciados.push(obj);
//                var fila = "<tr><td>" + obj.username + "</td><td>" + obj.games + "</td></tr>"
//                $("#viciadoss").append(fila);
//            });
//        },
//        error: function (e) {
//            if (e["responseJSON"] === undefined)
//                alert(emess);
//            else
//                alert(e["responseJSON"]["error"]);
//        }
//    });
//    $.ajax({
//        type: "get",
//        url: "GetPost", //canviar al Servlet després de comprovar que funciona.
//        dataType: "json",
//        success: function (jsn) {
//            $.each(jsn, function (index, value) {
//                var obj = new Object();
//                //var ident = value.nombre;
//                //obj.id = ident;
//                obj.username = value.username;
//                obj.score = value.score;
//                viciados.push(obj);
//                var fila = "<tr><td>" + obj.username + "</td><td>" + obj.score + "</td></tr>"
//                $("#scores").append(fila);
//            });
//        },
//        error: function (e) {
//            if (e["responseJSON"] === undefined)
//                alert(emess);
//            else
//                alert(e["responseJSON"]["error"]);
//        }
//    });
//    $.ajax({
//        type: "get",
//        url: "GetPost", //canviar al Servlet després de comprovar que funciona.
//        dataType: "json",
//        success: function (jsn) {
//            $.each(jsn, function (index, value) {
//                var obj = new Object();
//                //var ident = value.nombre;
//                //obj.id = ident;
//                obj.username = value.username;
//                viciados.push(obj);
//                var fila = "<tr><td>" + obj.username + "</td></tr>"
//                $("#onlines").append(fila);
//            });
//        },
//        error: function (e) {
//            if (e["responseJSON"] === undefined)
//                alert(emess);
//            else
//                alert(e["responseJSON"]["error"]);
//        }
//    });

    $("#submit").click(function () { //onclick event

        var url = "Register";
        var n = $("#usern").val(); //get name from input
        var e = $("#pass1").val(); //get age from input
        var j = $("#name").val();
        var m = $("#email").val();
        if (n != "" && e != "" && j != "" && m != "") {
            $.ajax({
                method: "POST",
                url: url,
                data: {username: n, password: e, nombre: j, email: m},
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

    $("#save").click(function () {

        peticionPost();

    });

    $("#load").click(function () {


        cargarConfiguraciones();
    });


    $("#entrar").click(function () { //onclick event

        var url = "LogIn";
        var emess = "Error desconocido";
        var n = $("#username").val(); //get name from input
        var e = $("#password").val(); //get age from input
        $.ajax({
            method: "POST",
            url: url,
            data: {username: n, password: e},
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

    document.getElementById("instrucciones").onclick = function () {
        mostrarInstrucciones();
    };
    document.getElementById("mScore").onclick = function () {
        mostrarScore();
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
    document.getElementById("dificultad").onclick = function () {
        cambiarDificultadbtn();
    }

    //CAMBIAR LA IMAGEN DE LA LUNA
    document.getElementById("modeloLuna").onclick = function () {
        cambiarLuna();

    }

    //CAMBIAR LA IMAGEN DE LA NAVE Y EL MOTOR
    document.getElementById("modeloNave").onclick = function () {
        cambiarNave();
    }


    //mostrar menú móvil
    $("#shown").click(function () {
        document.getElementsByClassName("c")[0].style.display = "block";
        stop();
    });
    //ocultar menú móvil
    $("#hidem").click(function () {
        document.getElementsByClassName("c")[0].style.display = "none";

    });

    //Empezar a mover la nave justo después de cargar la página

    var botonOnSmartphone = document.getElementById("botonOn");
    botonOnSmartphone.addEventListener("touchstart", handlerFunction, false);
    botonOnSmartphone.addEventListener("touchend", endingFunction, false);
    function handlerFunction(event) {
        motorOn();
    }
    function endingFunction(event) {
        motorOff();
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
    window.onkeyup = function (e) {
        motorOff();
    }
    
    peticionGetConfiguration();
  
    reiniciarJuego();
    start();    
});

//Definición de funciones
function start() {
    //cada intervalo de tiempo mueve la nave
    motorOff();
    timer = setInterval(function () {
        moverNave();
    }, dt * 1000);
}
function stop() {
    clearInterval(timer);
}

function moverNave() {
    if (fuel == 0) {
        a = g;
    }
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
    if (combustible > 0) {
        a = -g;
        document.getElementById("imgMotor").style.display = "block";
        if (timerFuel == null) {
            timerFuel = setInterval(function () {
                actualizarFuel();
            }, 100);
        }
    }
    if (combustible <= 0) {
        motorOff();
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
    combustible -= 1;
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

        var difficulty = dificultad;
        var fuel = combustible;
        var endtime = date.getSeconds();

        var n = difficulty; //get name from input
        var e = fuel; //get age from input
        var j = inittime;
        var m = endtime;
        var url = ""; //añadirURL
        var emess = "Error desconocido";
//        $.ajax({
//            method: "POST",
//            url: url,
//            data: {difficulty: n, fuel: e, inittime: j, endtime: m},
//            success: function (u) {
//                alert(u["mess"]);
//            },
//            error: function (e) {
//                if (e["responseJSON"] === undefined)
//                    alert(emess);
//                else
//                    alert(e["responseJSON"]["error"]);
//            }
//        });
    } else {
        document.getElementById("userWin").style.display = "block";
        eventosOff();

        var difficulty = dificultad;
        var fuel = combustible;
        var endtime = date.getSeconds();

        var n = difficulty; //get name from input
        var e = fuel; //get age from input
        var j = inittime;
        var m = endtime;
        var url; //añadirURL
        var emess = "Error desconocido";
//        $.ajax({
//            method: "POST",
//            url: url,
//            data: {difficulty: n, fuel: e, inittime: j, endtime: m},
//            success: function (u) {
//                alert(u["mess"]);
//            },
//            error: function (e) {
//                if (e["responseJSON"] === undefined)
//                    alert(emess);
//                else
//                    alert(e["responseJSON"]["error"]);
//            }
//        });
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
    inittime = date.getSeconds();
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
function mostrarScore() {
    document.getElementById("settings").style.display = "none";
    document.getElementById("score").style.display = "block";
    eventosOn();
}
function ocultarScore() {
    document.getElementById("score").style.display = "none";
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

function peticionGetConfiguration() {


    var url = "GetConfiguration";
    var emess = "hola ha fallado";

    $.ajax({
        type: "get",
        url: url,
        dataType: "json",

        success: function (u) {

            $.each(u, function (i, value) {

                var nombre = value.configName;

                addNombre(nombre);

                if (value.diffculty == 1) {
                    dificultad = 3;
                    cambiarDificultadbtn();
                } else if (value.difficulty == 2) {

                    dificultad = 1;
                    cambiarDificultadbtn();

                } else if (value.diffculty == 3) {

                    dificultad = 2;
                    cambiarDificultadbtn();
                    ;
                }

                if (value.spaceship == 1) {
                    modeloNave = 3;
                    cambiarNave();

                } else if (value.spaceship == 2) {
                    modeloNave = 1;
                    cambiarNave();
                } else if (value.spaceship == 3) {
                    modeloNave = 2;
                    cambiarNave();

                }

                if (value.moon == 1) {
                    modeloLuna = 3;
                    cambiarLuna();

                } else if (value.moon == 2) {
                    modeloLuna = 1;
                    cambiarLuna();
                } else if (value.moon == 3) {
                    modeloLuna = 2;
                    cambiarLuna();
                }

                var a = new Object();
                a.nombre = nombre;
                a.nivelDificultad = value.difficulty;
                a.modeloNave = value.spaceship;
                a.modeloLuna = value.moon;
                configuraciones.push(a);
                      

            });





        },
        error: function (e) {
            if (e["responseJSON"] === undefined)
                alert(emess);
            else
                alert(e["responseJSON"]["error"]);
        }
    });




}

function peticionPost() {

    var url = "CreateConfiguration";
    var emess = "Error desconocido";
    var f = $("#configname").val();
    var n = dificultad;
    var e = modeloNave;
    var r = modeloLuna;
    $.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        data: {configName: f, nivelDeDificultad: n, modeloNave: e, modeloLuna: r},

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


}

function cambiarDificultadbtn() {
    switch (dificultad) {
        case 1:
            combustible = 50;
            gasolinaTotal = 50;
            document.getElementById("dificultad").innerHTML = "Media";
            dificultad = 2;
            restart();
            break;
        case 2:
            combustible = 35;
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

function cambiarNave() {
    switch (modeloNave) {
        case 1:
            document.getElementById("imgNave").src = "img/mod3nave.png";
            document.getElementById("modeloNave").innerHTML = "Pixel";
            modeloNave = 2;
            restart();
            break;
        case 2:
            document.getElementById("imgNave").src = "img/mod2nave.png";
            //document.getElementById("imgMotor").src = "img/motor.gif";
            document.getElementById("modeloNave").innerHTML = "Freezer";
            modeloNave = 3;
            restart();
            break;
        case 3:
            document.getElementById("imgNave").src = "img/nave.png";
            document.getElementById("modeloNave").innerHTML = "Cohete";
            modeloNave = 1;
            restart();
            break;


    }
}

function cambiarLuna() {
    switch (modeloLuna) {
        case 1:
            document.getElementById("luna").src = "img/mod2luna.png";
            document.getElementById("modeloLuna").innerHTML = "Marte";
            modeloLuna = 2;
            break;
        case 2:
            document.getElementById("luna").src = "img/mod3luna.png";
            document.getElementById("modeloLuna").innerHTML = "Venus";
            modeloLuna = 3;
            break;
        case 3:
            document.getElementById("luna").src = "img/luna.png";
            document.getElementById("modeloLuna").innerHTML = "Luna";
            modeloLuna = 1;
            break;
    }
}

function cambiarDificultad(mod) {
    switch (mod) {
        case 1:
            combustible = 50;
            gasolinaTotal = 50;
            document.getElementById("dificultad").innerHTML = "Media";
            dificultad = 2;
            restart();
            break;
        case 2:
            combustible = 35;
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

function cambiarModeloNave(mod) {
    switch (mod) {
        case 1:
            document.getElementById("imgNave").src = "img/mod3nave.png";
            document.getElementById("modeloNave").innerHTML = "Pixel";
            modeloNave = 2;
            restart();
            break;
        case 2:
            document.getElementById("imgNave").src = "img/mod2nave.png";
            //document.getElementById("imgMotor").src = "img/motor.gif";
            document.getElementById("modeloNave").innerHTML = "Freezer";
            modeloNave = 3;
            restart();
            break;
        case 3:
            document.getElementById("imgNave").src = "img/nave.png";
            document.getElementById("modeloNave").innerHTML = "Cohete";
            modeloNave = 1;
            restart();
            break;


    }
}

function cambiarModeloLuna(mod) {
    switch (mod) {
        case 1:
            document.getElementById("luna").src = "img/mod2luna.png";
            document.getElementById("modeloLuna").innerHTML = "Marte";
            modeloLuna = 2;
            break;
        case 2:
            document.getElementById("luna").src = "img/mod3luna.png";
            document.getElementById("modeloLuna").innerHTML = "Venus";
            modeloLuna = 3;
            break;
        case 3:
            document.getElementById("luna").src = "img/luna.png";
            document.getElementById("modeloLuna").innerHTML = "Luna";
            modeloLuna = 1;
            break;
    }
}

function addNombre(nom) {


    var op = document.createElement("option");
    op.text = nom;
    document.getElementById("selconf").appendChild(op);

}

function cargarConfiguraciones() {

    var i = document.getElementById("selconf").selectedIndex;


    var dificultad = parseInt(configuraciones[i].nivelDificultad);
    var nave = parseInt(configuraciones[i].modeloNave);
    var luna = parseInt(configuraciones[i].modeloLuna);



    cambiarModeloLuna(luna);
    cambiarModeloNave(nave);
    cambiarDificultad(dificultad);


}