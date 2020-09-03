var idProfesor = localStorage.getItem("idProfesor");
var token = localStorage.getItem("token");

$(function() {

    /**
     * Manejador de evento para registro de una línea
     */
    $("#registro-linea").submit(function(e){

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
                type: "POST",
                url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+ idProfesor +"/lineas/", // set your URL here
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
                    console.log(xhr.status);
                    console.log(errorThrown);
                    if (errorThrown == "Petición incorrecta"){
                        showInvalido("#tituloLinea","#feedback-titulo");
                        $("#feedback-titulo").html("Ya existe una línea con el mismo titulo");
                    }

                }
            }).done(function(response) {
                // do something with the received data/response
                console.log(linea);
                $('#success-linea').modal('show');
                $('#tituloLinea').val("");
                $('#descripcion').val("");
                $('#number').val("");

                $('#tituloLinea').removeClass("is-valid");
                $('#descripcion').removeClass("is-valid");
                $('#number').removeClass("is-valid");
                $('#tipoLinea').removeClass("is-valid");
            });
       
        }
        
    });

});





