var idProfesor = localStorage.getItem("idProfesor"); 
var idLinea = localStorage.getItem("idLinea");
var token = localStorage.getItem("token");

$(function() {

    /**
     * Manejador de evento para registro de un estudiante
     */
    $("#registro-estudiante").submit(function(e){

        // stop form from submitting
        e.preventDefault();
    
        // Grab all values
        var estudiante = new Object();
        estudiante.nombre = $('#nombre').val();
        estudiante.apellidos = $('#apellidos').val();
        estudiante.urlGit = $('#urlGit').val();
        estudiante.fechaEstimada = $('#fechaEstimada').val();

        var numEstudiantes = localStorage.getItem("numEstudiantes");
        var maxEstudiantes = localStorage.getItem("maxEstudiantes");
        
        if (validarEstudiante(estudiante)){
            if (numEstudiantes == maxEstudiantes){
                $(".modal-title").html("Registro fallido");
                $(".modal-body p").html("Ya se han registrado el máximo número de estudiantes permitidos");
                $('#success-estudiante').modal('show');
                $('#nombre').val("");
                $('#apellidos').val("");
                $('#urlGit').val("");
                $('#fechaEstimada').val("");
    
                $('#nombre').removeClass("is-valid");
                $('#apellidos').removeClass("is-valid");
                $('#urlGit').removeClass("is-valid");
                $('#fechaEstimada').removeClass("is-valid");
            }
            else {
                $.ajax({
                    type: "POST",
                    url: "http://"+ configOptions.server +":"+ configOptions.port +"/jersey-webapp/webapi/profesores/"+ idProfesor +"/lineas/"+ idLinea +"/estudiantes/", // set your URL here
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
                    $(".modal-title").html("Enhorabuena");
                    $(".modal-body p").html("Se ha registrado el nuevo estudiante correctamente");
                    $('#success-estudiante').modal('show');
                    $('#nombre').val("");
                    $('#apellidos').val("");
                    $('#urlGit').val("");
                    $('#fechaEstimada').val("");
    
                    $('#nombre').removeClass("is-valid");
                    $('#apellidos').removeClass("is-valid");
                    $('#urlGit').removeClass("is-valid");
                    $('#fechaEstimada').removeClass("is-valid");
                    localStorage.setItem("numEstudiantes",parseInt(numEstudiantes) + 1);
                });
            }
           
        }
        
    });
});