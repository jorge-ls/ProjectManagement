var idProfesor = localStorage.getItem("idProfesor"); 
var idLinea = localStorage.getItem("idLinea");
var idEstudiante = localStorage.getItem("idEstudiante");
var token = localStorage.getItem("token");

/**
 * Método para leer un fichero 
 * @param {file} file El fichero 
 * @param {string} documento Cadena que contiene el nombre del fichero y su contenido en base 64
 * @param {Object} reunion La reunion
 */
function readFile(file,documento,reunion) {
    var reader = new FileReader();
    
    //Cuando la lectura del fichero finaliza se registra la reunión
    reader.onloadend = function (e) {
        console.log(e.target.result);
        documento += e.target.result.split(",")[1];
        reunion.memoria = documento;
        console.log(reunion.memoria);
        registrarReunion(reunion);
    }

    reader.readAsDataURL(file);
}

/**
 * Función para registro de una reunión
 * @param {Object} reunion La reunión a registrar
 */
function registrarReunion(reunion){

    $.ajax({
        type: "POST",
        url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+ idProfesor +"/lineas/"+ idLinea +"/estudiantes/"+ idEstudiante + "/reuniones/",  // set your URL here
        //dataType: 'json',
        contentType: "application/json",
        data: JSON.stringify(reunion),
        beforeSend: function(request) {
            request.setRequestHeader("Authorization","Bearer "+token);
        }, 
        error: function (xhr, textStatus, errorThrown) {
            console.log('Error:' + xhr.responseText);
            var detalles = "";
            for (var id in xhr) {
               detalles += "<b>" + id + "</b>: " + xhr[id] + "<br/>";
            }
        }
    }).done(function(response) {
        // do something with the received data/response
        console.log(reunion);
        $('#success-reunion').modal('show');
        $('#titulo').val("");
        $('#tipoReunion').val("");
        $('#fecha').val("");
        $('#duracion').val("");
        $('#comentario').val("");
        $('#memoria').val("");

        $('#titulo').removeClass("is-valid");
        $('#tipoReunion').removeClass("is-valid");
        $('#duracion').removeClass("is-valid");
        $('#fecha').removeClass("is-valid");
        $('#comentario').removeClass("is-valid");
        $('#memoria').removeClass("is-valid");
    });
}

$(function() {
    /**
     * Manejador de evento para el registro de una reunión
     */
    $("#registro-reunion").submit(function(e){

        // stop form from submitting
        e.preventDefault();
    
        // Grab all values

        var file = $('#memoria').prop("files")[0];
        var reunion = new Object();
        reunion.titulo = $('#titulo').val();
        reunion.tipoReunion = $('#tipoReunion').val();
        reunion.fecha = $('#fecha').val();
        reunion.minutos = $('#duracion').val();
        reunion.comentario = $('#comentario').val();

        if (validarReunion(reunion,"create")){
            var documento = file.name + ",";
            readFile(file,documento,reunion);
        }
        
    });

    $('#memoria').focusout(function() {
        var file = $('#memoria').prop("files")[0];
        console.log("Fichero: "+file);
        if (file){
            console.log("El nombre del fichero es "+file.name);
    }
        
    });
       
});