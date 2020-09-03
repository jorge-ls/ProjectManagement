var numEstudiantes;
var idEstudiante;
var idProfesor = localStorage.getItem("idProfesor");
var idLinea = localStorage.getItem("idLinea");
var token = localStorage.getItem("token");

/**
 * Método para obtener los datos de una línea
 */
function getLinea(){
    $.ajax({
        url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+ idProfesor +"/lineas/"+ idLinea,
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
        $('#tituloLinea').val(data["titulo"]);
		$('#descripcion').val(data["descripcion"]);
		$('#number').val(data["maxEstudiantes"]);
        $("#tipoLinea option[value="+ data["tipoLinea"] +"]").attr("selected",true);
        numEstudiantes = data["numEstudiantes"];
        localStorage.setItem("maxEstudiantes",data["maxEstudiantes"]);
        localStorage.setItem("numEstudiantes",numEstudiantes);
        console.log(data);
    });
}

/**
 * Función que obtiene y muestra en la pantalla el listado de estudiantes asociados a una línea
 */
function cargarEstudiantesLinea(){
    $.ajax({
        url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+ idProfesor +"/lineas/"+ idLinea +"/estudiantes/",
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
        $("#lista-estudiantes").empty();
        console.log(numEstudiantes);
        $("#numEstudiantes").html(numEstudiantes);
        for (var estudiante in data)
        {
            var id_estudiante = data[estudiante]["id"];
            var nombre_estudiante = data[estudiante]["nombre"]+ " " + data[estudiante]["apellidos"];

            var li = '<div id='+ id_estudiante +' class="col-10 col-sm-10 col-md-10 bg-dark text-white container-list__item">' +
                        '<div class="row align-items-center my-row">' +
                            '<div class="col-sm-7 col-md-7 col-lg-7 col-xl-8">' +
                                '<div class="row align-items-center my-row">' +
                                    '<h4 class="col-xl-12 entry-title">' + nombre_estudiante +'</h4>' +
                                    '<h4 class="col-xl-3 entry-title">( ' + data[estudiante]["minutosTotales"] + ' min )</h4>' +
                                    '<h4 class="col-xl-5 entry-title">' + data[estudiante]["estadoTrabajo"] +'</h4>'+
                                '</div>' +
                            '</div>' +
                            '<div class="col-sm-4 col-md-5 col-lg-5 col-xl-4">' +
                                '<div class="row botton-container">' +
                                    '<a class="detalles-estudiante col-5 col-sm-12 col-md-5 btn btn-outline-success botton-container__input" data-id='+ id_estudiante + ' href="consultaEstudiante.html">Ver detalles</a>' +
                                    '<button class="delete-estudiante col-5 col-sm-12 col-md-5 btn btn-outline-success botton-container__input" data-id='+ id_estudiante + ' type="button" data-toggle="modal" data-target="#eliminarEstudiante">Eliminar</button>' +
                                '</div>' +
                            '</div>' +
                        '</div>' +
                    '</div>';
            $("#lista-estudiantes").append(li);
        } 

        $(".delete-estudiante").on("click", function(){
            idEstudiante = $(this).attr("data-id");
            console.log("El id del estudiante seleccionado es: "+idEstudiante);
        });

        //Manejador de evento para mostrar los datos de un estudiante
        $(".detalles-estudiante").on("click", function(){
            localStorage.setItem("idEstudiante",$(this).attr("data-id"));
        });

        console.log(data);
    });
}

/**
 * Función para eliminar los datos de un estudiante
 */
function deleteEstudiante(){
    $.ajax({
        type: "DELETE",
        url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+ idProfesor +"/lineas/"+ idLinea +"/estudiantes/"+ idEstudiante,
        success: function (data, textStatus, xhr) {
            console.log(data);
        },
        beforeSend: function(request) {
            request.setRequestHeader("Authorization","Bearer "+token);
        },
        error: function () {
            console.log(idEstudiante);
        }
    }).done(function( response ) {
        // do something with the received data/response
        cargarEstudiantesLinea();
    });
}

$(function() {
    getLinea();
    cargarEstudiantesLinea();

    /**
     * Manejador de evento para la eliminacion de un estudiante 
     */
    $("#button-eliminar").on("click", function(){
        deleteEstudiante();
        numEstudiantes = numEstudiantes - 1;
        $("#numEstudiantes").html(numEstudiantes);
        localStorage.setItem("numEstudiantes",numEstudiantes);
    });

    /**
     * Manejador de evento para la actualización de una línea
     */
    $("#consulta-linea").submit(function(e){

        // stop form from submitting
        e.preventDefault();
    
        // Grab all values
        var linea = new Object();
        linea.titulo = $('#tituloLinea').val();
        linea.descripcion = $('#descripcion').val();
        linea.maxEstudiantes = $('#number').val();
        linea.tipoLinea = $('#tipoLinea').val();
        
        if (validarLinea(linea)){
            $.ajax({
                type: "PUT",
                url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+ idProfesor +"/lineas/"+ idLinea, // set your URL here
                //dataType: 'json',
                contentType: "application/json",
                data: JSON.stringify(linea),
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
                console.log(linea);
                $('#update-success-linea').modal('show');
                $('#tituloLinea').removeClass("is-valid");
                $('#descripcion').removeClass("is-valid");
                $('#number').removeClass("is-valid");
                $('#tipoLinea').removeClass("is-valid");
            });
        }
        
    });
});