var idProfesor;

/**
 * Función para obtener los datos de un profesor
 */
function getProfesor(){
    $.ajax({
        url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+ idProfesor,
        error: function (xhr, textStatus, errorThrown) {
            console.log('Error:' + xhr.responseText);
            var detalles = "";
            for (var id in xhr) {
               detalles += "<b>" + id + "</b>: " + xhr[id] + "<br/>";
            }
            alert(detalles);
        }

    }).done(function(data) {
        
    });
}


$(function() {

    $("#success-registro").on("click", function(){
        window.location.href = 'login.html';
    });

    /**
     * Manejador de evento de registro de un profesor
     */
    $("#registro-profesor").submit(function(e){

        // stop form from submitting
        e.preventDefault();
    
        // Grab all values
        var profesor = new Object();
        profesor.nombre = $('#name').val();
        profesor.apellidos = $('#surnames').val();
        profesor.usuario = $('#username').val();
        profesor.contraseña = $('#userpass').val();

        var passRepetida = $('#repeatpass').val();

        if (validarProfesor(profesor)){
            $.ajax({
                type: "POST",
                url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/", // set your URL here
                //dataType: 'json',
                contentType: "application/json",
                data: JSON.stringify(profesor), 
                error: function (xhr, textStatus, errorThrown) {
                    console.log('Error:' + xhr.responseText);
                    var detalles = "";
                    for (var id in xhr) {
                       detalles += "<b>" + id + "</b>: " + xhr[id] + "<br/>";
                    }
                    if (errorThrown == "Petición incorrecta"){
                        showInvalido("#username","#feedback-username");
                        $("#feedback-username").html("Ya existe un usuario con el mismo nombre");
                    }
                }
            }).done(function(data) {
                // do something with the received data/response
                console.log(profesor);
                $('#success-registro').modal('show');
                $('#name').val("");
                $('#surnames').val("");
                $('#username').val("");
                $('#userpass').val("");
                $('#repeatpass').val("");

                $('#name').removeClass("is-valid");
                $('#surnames').removeClass("is-valid");
                $('#username').removeClass("is-valid");
                $('#userpass').removeClass("is-valid");
                $('#repeatpass').removeClass("is-valid");
    
                //window.location.href = 'login.html';
            });
        }
        
    });

    /**
     * Manejador de evento de inicio de sesión de un profesor
     */
    $("#login-profesor").submit(function(e){
        
        // stop form from submitting
        e.preventDefault();
        var credenciales = new Object();
        credenciales.usuario = $("#username").val();
        credenciales.contraseña = $("#userpass").val();
         
        $.ajax({
            type: "POST",
            url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/login", // set your URL here
            //dataType: 'json',
            contentType: "application/json",
            data: JSON.stringify(credenciales), 
            error: function (xhr, textStatus, errorThrown) {
                console.log('Error:' + xhr.responseText);
                var detalles = "";
                for (var id in xhr) {
                   detalles += "<b>" + id + "</b>: " + xhr[id] + "<br/>";
                }
                console.log(errorThrown);
                if (errorThrown == "No Autorizado"){
                    $("#feedback-login").css("display","flex");
                }
            }
        }).done(function(data,status,xhr) {
            // do something with the received data/response
            var autorizacion = xhr.getResponseHeader("Authorization");
            var token = autorizacion.split(" ").pop();
            localStorage.setItem("token",token);
            $('#username').val("");
            $('#userpass').val("");
            var payload = window.atob(token.split(".")[1]);
            idProfesor =  JSON.parse(payload)["idProfesor"];
            localStorage.setItem("idProfesor",idProfesor);
            window.location.href = 'index.html';
    
        });
    });

});