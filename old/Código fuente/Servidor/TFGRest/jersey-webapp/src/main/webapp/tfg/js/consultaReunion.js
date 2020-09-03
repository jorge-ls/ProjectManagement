var idLinea = localStorage.getItem("idLinea");
var idEstudiante = localStorage.getItem("idEstudiante");
var idReunion = localStorage.getItem("idReunion");
var idProfesor = localStorage.getItem("idProfesor");
var token = localStorage.getItem("token");

/**
 * Función para obtener los datos de una reunión
 */
function getReunion(){
    $.ajax({
        url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+ idProfesor +"/lineas/"+ idLinea +"/estudiantes/"+ idEstudiante +"/reuniones/"+ idReunion,
        beforeSend: function(request) {
            request.setRequestHeader("Authorization","Bearer "+token);
        },
        error: function (xhr, textStatus, errorThrown) {
            console.log('Error:' + xhr.responseText);
            var detalles = "";
            for (var id in xhr) {
               detalles += "<b>" + id + "</b>: " + xhr[id] + "<br/>";
            }
            alert(detalles);
        }

    }).done(function(data) {
        $('#titulo').val(data["titulo"]);
		$('#tipoReunion').val(data["tipoReunion"]);
        var date = new Date(data["fecha"]);
        let dia = date.getDate();
        if (dia < 10){
            dia = "0"+ dia;
        }
        let mes = date.getMonth()+1;
        if (mes < 10){
            mes = "0"+ mes;
        }
        let year = date.getFullYear();
        let fecha = year + "-" + mes + "-" + dia; 
        console.log(fecha);
        $('#fecha').val(fecha);
        $('#duracion').val(data["minutos"]);
        $('#comentario').val(data["comentario"]);
        $('#urlMemoria').text(data["urlMemoria"]);
        console.log("El contenido del campo url es: "+$("#urlMemoria").text());
        //var url = $("#urlMemoria").val();
        //var fileName = url.split("\\").pop();
        //console.log("El nombre del fichero a seleccionar es: "+fileName);
        console.log(data);
    });
}

$(function() {
    getReunion();

    /**
     * Manejador de evento para actualizar los datos de una reunión
     */
    $("#consulta-reunion").submit(function(e){

        // stop form from submitting
        e.preventDefault();
    
       /*  var urlInforme = $('#urlMemoria').val();
        var queryParams = urlInforme.split("?").pop();
        var fileName = queryParams.split("=").pop(); */

        // Grab all values
        var reunion = new Object();
        reunion.titulo = $('#titulo').val();
        reunion.tipoReunion = $('#tipoReunion').val();
        reunion.fecha = $('#fecha').val();
        reunion.minutos = $('#duracion').val();
        //reunion.urlMemoria = fileName;
        reunion.comentario = $('#comentario').val();
        
        if (validarReunion(reunion,"update")){
            $.ajax({
                type: "PUT",
                url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+ idProfesor +"/lineas/"+ idLinea +"/estudiantes/"+ idEstudiante + "/reuniones/"+ idReunion,  // set your URL here
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
                $('#success-update-reunion').modal('show');
        
                $('#titulo').removeClass("is-valid");
                $('#tipoReunion').removeClass("is-valid");
                $('#duracion').removeClass("is-valid");
                $('#fecha').removeClass("is-valid");
                $('#comentario').removeClass("is-valid");
            });
        }
        
    });

    /**
     * Manejador de evento para descargar un fichero
     */
    $("#urlMemoria").on("click", function(){
        var fileName = $('#urlMemoria').text();
        console.log("La url del informe a descargar es: "+ fileName);
        $.ajax({
            url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+ idProfesor +"/fichero?fileName="+fileName,
            beforeSend: function(request) {
                request.setRequestHeader("Authorization","Bearer "+token);
            },
            xhrFields: {
                responseType: 'blob'
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log('Error:' + xhr.responseText);
                var detalles = "";
                for (var id in xhr) {
                   detalles += "<b>" + id + "</b>: " + xhr[id] + "<br/>";
                }
                alert(detalles);
                console.log(textStatus);
                console.log(errorThrown);
            }
    
        }).done(function(data,textStatus,xhr) {
                var blob = new Blob([data], { type: "application/octet-stream" });
                var a = document.createElement("a");
                var url = window.URL.createObjectURL(blob);
                a.href = url;
                a.download = fileName;
                document.body.append(a);
                a.click();
                a.remove();
                console.log(url);
                //window.open(url);
                window.URL.revokeObjectURL(url);
        });
    });
});


