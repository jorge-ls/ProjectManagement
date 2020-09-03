$(function() {
    /**
     * Manejador de evento para eliminar el id de un profesor y su token asociado 
     * del localstorage del navegador
     */
    $("#btn-logout").on("click", function(){
        localStorage.removeItem("token");
        localStorage.removeItem("idProfesor");
    });
});
