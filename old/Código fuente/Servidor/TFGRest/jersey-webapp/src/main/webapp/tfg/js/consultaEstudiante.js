var tiempoTotal = 0;
var idProfesor = localStorage.getItem("idProfesor");
var idLinea = localStorage.getItem("idLinea");
var idEstudiante = localStorage.getItem("idEstudiante");
var token = localStorage.getItem("token");
var idReunion;
var minutosReunion;

/**
 * Función para obtener los datos de un estudiante
 */
function getEstudiante(){
    $.ajax({
        url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+ idProfesor +"/lineas/"+ idLinea +"/estudiantes/"+ idEstudiante,
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
        $('#nombre').val(data["nombre"]);
		$('#apellidos').val(data["apellidos"]);
        $('#urlGit').val(data["urlGit"]);
        var date = new Date(data["fechaEstimada"]);
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
        $('#fechaEstimada').val(fecha);
        $('#estadoTrabajo strong').text(data["estadoTrabajo"]);
        //console.log($('#estadoTrabajo strong').text());
        localStorage.setItem("estadoTrabajo",$('#estadoTrabajo strong').text());

        if (data["estadoTrabajo"] == "ENTREGADO"){
            $(".input-calificar").css("display","flex");
            //$(".input-calificar").show();
            $("#vocales").val(data["vocales"])
            $('#informe').text(data["urlInforme"]);
            $('#memoriaFinal').text(data["urlMemoriaFinal"]);
        }
        
        tiempoTotal = data["minutosTotales"];
        $("#tiempoTotal").html("( "+ tiempoTotal + " min )");
        console.log(data);
    });
}

/**
 * Función para eliminar una reunión
 */
function deleteReunion(){
    $.ajax({
        type: "DELETE",
        url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+ idProfesor +"/lineas/"+ idLinea +"/estudiantes/"+ idEstudiante +"/reuniones/"+ idReunion,
        success: function (data, textStatus, xhr) {
            console.log(data);
        },
        beforeSend: function(request) {
            request.setRequestHeader("Authorization","Bearer "+token);
        },
        error: function () {
            console.log('Error:' + xhr.responseText);
            var detalles = "";
            for (var id in xhr) {
               detalles += "<b>" + id + "</b>: " + xhr[id] + "<br/>";
            }
            alert(detalles);
        },
    }).done(function( response ) {
        // do something with the received data/response
        cargarReunionesEstudiante();
    });
}

/**
 * Función para obtener y mostrar en pantalla las reuniones pertenecientes a un estudiante
 */
function cargarReunionesEstudiante(){
    $.ajax({
        url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+ idProfesor +"/lineas/"+ idLinea +"/estudiantes/"+ idEstudiante + "/reuniones",
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
        $("#lista-reuniones").empty();
        
        for (var reunion in data)
        {
            var id_reunion = data[reunion]["id"];
            var minutos = data[reunion]["minutos"];

            var li = '<div id='+ id_reunion + ' class="col-10 col-sm-10 col-md-10 bg-dark container-list__item">' +
                        '<div class="row align-items-center my-row">' +
                            '<h4 class="col-sm-7 col-md-6 col-lg-6 col-xl-8 entry-title">'+ data[reunion]["titulo"] + '<strong> ( '+ minutos +' min )</strong></h4>' +
                            '<div class="col-sm-4 col-md-5 col-lg-5 col-xl-4">' +
                                '<div class="row botton-container">' +
                                    '<a class="detalles-reunion col-5 col-sm-12 col-md-5 btn btn-outline-success botton-container__input" data-id= '+ id_reunion +' href="consultaReunion.html">Ver detalles</a>' +
                                    '<button class="delete-reunion col-5 col-sm-12 col-md-5 btn btn-outline-success botton-container__input" data-id= '+ id_reunion +' data-minutos= '+ minutos +' type="button" data-toggle="modal" data-target="#eliminarReunion">Eliminar</button>' +
                                '</div>' +
                            '</div>' +
                        '</div>' +
                    '</div>';
             
            $("#lista-reuniones").append(li);
        } 

        $(".delete-reunion").on("click", function(){
            idReunion = $(this).attr("data-id");
            minutosReunion = $(this).attr("data-minutos");
            console.log("El id de la reunion seleccionada es: "+ idReunion);
        });

        //Manejador de evento para mostrar los detalles de una reunión
        $(".detalles-reunion").on("click", function(){
            localStorage.setItem("idReunion",$(this).attr("data-id"));
        });

        console.log(data);
    });
}

/**
 * Función para descargar un fichero
 * @param {string} fileName El nombre del fichero
 */
function downloadFile(fileName){
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

    }).done(function(data) {
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
}

$(function() {
    console.log(idEstudiante);
    getEstudiante();
    cargarReunionesEstudiante();

    /**
     * Manejador de evento para eliminar una reunión
     */
    $("#button-eliminar").on("click", function(){
        deleteReunion();
        tiempoTotal = tiempoTotal - minutosReunion;
        $("#tiempoTotal").html("( "+tiempoTotal + " min )");
    });

    $("#informe").on("click", function(){
        var nombreInforme = $('#informe').text();
        console.log("La url del informe a descargar es: "+ nombreInforme);
        downloadFile(nombreInforme);
    });

    $("#memoriaFinal").on("click", function(){
        var memoriaFinal = $('#memoriaFinal').text();
        console.log("La url del informe a descargar es: "+ memoriaFinal);
        downloadFile(memoriaFinal);
    });


    /**
     * Manejador de evento para actualizar un estudiante
     */
    $("#consulta-estudiante").submit(function(e){

        // stop form from submitting
        e.preventDefault();
    
        // Grab all values
        var estudiante = new Object();
        estudiante.nombre = $('#nombre').val();
        estudiante.apellidos = $('#apellidos').val();
        estudiante.urlGit = $('#urlGit').val();
        estudiante.fechaEstimada = $('#fechaEstimada').val();
        estudiante.vocales = $('#vocales').val();
        //estudiante.informe = $("#informe").text();
        //estudiante.memoriaFinal = $("#memoriaFinal").text();
        
        if (validarEstudiante(estudiante)){
            $.ajax({
                type: "PUT",
                url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+ idProfesor +"/lineas/"+ idLinea +"/estudiantes/"+ idEstudiante, // set your URL here
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
                    alert(detalles);
                }
            }).done(function(response) {
                // do something with the received data/response
                console.log(estudiante);
                $('#update-success-estudiante').modal('show');
    
                $('#nombre').removeClass("is-valid");
                $('#apellidos').removeClass("is-valid");
                $('#urlGit').removeClass("is-valid");
                $('#fechaEstimada').removeClass("is-valid");
                $('#vocales').removeClass("is-valid");
            });
        }
        
    });

});