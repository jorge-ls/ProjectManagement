var leidos = 0;
var numFileInput;
var datosMemFinal;
var datosInforme;

/**
 * Método para realizar la lectura de los ficheros solicitados y la posterior calificación 
 * del estudiante
 * @param {string} tipo El tipo de fichero solicitado 
 * @param {file} file El fichero a leer
 * @param {string} datos Cadena que contiene el nombre del fichero y su contenido en base 64
 */
function readFile(tipo,file,datos) {
    var reader = new FileReader();
    
    reader.onloadend = function (e) {
        //console.log(e.target.result);
        datos += e.target.result.split(",")[1];
        if (tipo == "informe"){
            datosInforme = datos;
        }
        else if (tipo == "memoria"){
            datosMemFinal = datos;
        }
        console.log(datos);
        leidos = leidos + 1;
        console.log("El numero de ficheros leidos es: "+leidos);
        if (leidos == numFileInput){
            console.log("Se han leido todos los ficheros");
            calificarEstudiante();
        }
        
    }
    reader.readAsDataURL(file);
}

/**
 * Función para realizar la calificación de un estudiante
 */
function calificarEstudiante(){
    // Grab all values
    var estudiante = new Object();
    estudiante.vocales = $('#vocales').val();
    estudiante.memFinal = datosMemFinal;
    estudiante.informe = datosInforme;

    console.log("Datos memoria final "+datosMemFinal);
    console.log("Datos informe: "+datosInforme);

    var idProfesor = localStorage.getItem("idProfesor");
    var idLinea = localStorage.getItem("idLinea");
    var idEstudiante = localStorage.getItem("idEstudiante");
    var token = localStorage.getItem("token");

    $.ajax({
        type: "PUT",
        url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+ idProfesor +"/lineas/"+ idLinea +"/estudiantes/"+ idEstudiante +"/calificacion",
        //dataType: 'json',
        contentType: "application/json",
        data: JSON.stringify(estudiante),
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
        console.log(estudiante);
        $('#success-calificar').modal('show');
        $('#vocales').val("");
        $('#informe').val("");
        $('#memoriaFinal').val("");

        $('#vocales').removeClass("is-valid");
        $('#informe').removeClass("is-valid");
        $('#memoriaFinal').removeClass("is-valid");
    });
}

$(function() {

    /*numFileInput = $(".file-input").length;
    console.log("El numero de entradas de tipo input es: "+numFileInput); */

    /**
     * Manejador de evento para realizar la calificación de un estudiante
     */
    $("#calificar-estudiante").submit(function(e){

        // stop form from submitting
        e.preventDefault();
    
        var vocales = $('#vocales').val();
        var informe = $('#informe').prop("files")[0];
        var memoriaFinal = $('#memoriaFinal').prop("files")[0];
        
        if (validarCalificacion(vocales)){
            console.log("El nombre del fichero seleccionado es: "+ memoriaFinal.name);
            datosMemFinal = memoriaFinal.name + ",";
            numFileInput = $(".file-input").length;

            console.log("El nombre del fichero seleccionado es: "+ informe.name);
            datosInforme = informe.name + ",";

            readFile("informe",informe,datosInforme);
            readFile("memoria",memoriaFinal,datosMemFinal);
        }
        
    });

});