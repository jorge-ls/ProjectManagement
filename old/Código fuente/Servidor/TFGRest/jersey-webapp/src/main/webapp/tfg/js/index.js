var idLinea;
var idProfesor = localStorage.getItem("idProfesor");
var token = localStorage.getItem("token");

/**
 * Función para cargar las líneas pertenecientes a un profesor
 */
function cargarLineas() {
    $.ajax({
        url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+ idProfesor + "/lineas/",
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
        $("#lista-lineas").empty();
        for (var linea in data)
        {
            var id_linea = data[linea]["id"];

            var li = '<div id='+ id_linea +' class="col-10 col-sm-10 col-md-10 col-lg-10 bg-dark text-white container-list__item">' +
                     '<div class="row align-items-center my-row">' +
                        '<h4 class="col-12 col-sm-7 col-md-6 col-lg-6 col-xl-8 entry-title">'+ data[linea]["titulo"] +'<strong> ('+ data[linea]["numEstudiantes"] +'/'+ data[linea]["maxEstudiantes"] +')</strong></h4>' +
                        '<div class="col-12 col-sm-4 col-md-5 col-lg-5 col-xl-4 my-col">' +
                            '<div class="row botton-container">' +
                                '<a class="detalles-linea col-5 col-sm-12 col-md-5 col-lg-5 btn btn-outline-success botton-container__input"  data-id='+ id_linea + ' href="consultaTFG.html">Ver detalles</a>' +
                                '<button class="delete-linea col-5 col-sm-12 col-md-5 btn btn-outline-success botton-container__input" type="button" data-id='+ id_linea + ' data-toggle="modal" data-target="#eliminarLinea">Eliminar</button>'
                            '</div>'
                        '</div>'
                     '</div>'
                     '</div>';
             
            $("#lista-lineas").append(li);
        }

        /* $("#lista-lineas").paginathing({
            perPage: 5,
        }) */

        $(".delete-linea").on("click", function(){
            idLinea = $(this).attr("data-id");
        });

        $(".detalles-linea").on("click", function(){
            //idLinea = $(this).attr("data-id");
            localStorage.setItem("idLinea",$(this).attr("data-id"));
            //console.log("El id de la linea seleccionada es: "+idLinea);
        });

        console.log(data);
    });
}

/**
 * Función para mostrar en pantalla nombre y apellidos de un profesor
 */
function cargarNombreProfesor() {
    $.ajax({
        url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+idProfesor,
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
        var profesor = data["nombre"] +" "+ data["apellidos"];
        $("#nombre-profesor").html(profesor);
        console.log(data);
    });
}

/**
 * Función para eliminar una línea
 * @param {number} idLinea El identificador de la línea
 */
function deleteLinea(idLinea){
    $.ajax({
        type: "DELETE",
        url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+ idProfesor +"/lineas/" + idLinea,
        success: function (data, textStatus, xhr) {
            console.log(data);
        },
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
    }).done(function( response ) {
        // do something with the received data/response
        cargarLineas();
        //location.reload();
    });
}


$(function() {
    //console.log(idProfesor);

    cargarNombreProfesor();
    cargarLineas();
    $("#button-eliminar").on("click", function(){
        deleteLinea(idLinea);
    });
    console.log("El token obtenido es: "+token);

});

