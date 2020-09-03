var expTitulo = new RegExp("^[a-zA-Zñáéíóú]+(\\s[a-zA-Zñáéíóú\\s]+)*$");
var expDescripcion = new RegExp("^[a-zA-Zñáéíóú0-9,\\.\\/]+(\\s[a-zA-Zñáéíóú0-9,\\.\\/\\s]+)*$");
var expNombre = new RegExp("^[a-zA-Zñáéíóú]+\\s?[a-zA-Zñáéíóú\\s]+$");
var expApellidos = new RegExp("^[a-zA-Zñáéíóú]+\\s[a-zA-Zñáéíóú\\s]+$");
var expUrlGit = new RegExp("^https?:\\/\\/[a-zA-Z0-9_@\\.-]+\\/[a-zA-Z0-9_-]+\\/[a-zA-Z_-]+\\.git$");
var expFecha = new RegExp("^\\d{4}\\-\\d{2}\\-\\d{2}$");
var expVocales = new RegExp("^[a-zA-Zñáéíóú\\n]+(\\s[a-zA-Zñáéíóú\\n\\s]+)*$");
var expUsuario = new RegExp("^[a-zA-Z0-9_\\-]+$");
var expPassword = new RegExp("^[a-zA-Zñ0-9]+$");
//var expUrl = new RegExp("^https?:\\/\\/localhost:8080\\/tfg\\/download\\?fileName=[a-zA-Z0-9ñáéíóú_\\-]+\\.[a-z]+$");

//var regex = new RegExp("[^%&#@,-_:\.\d]+(\s*[^%&#@,-_:\.\d]*)*");

/**
 * Función para indicar que el valor introducido en una entrada es correcto
 * @param {string} idElemento El identificador del elemento input
 * @param {string} feedback El identificador del mensaje de éxito mostrado
 */
function showValido(idElemento,feedback){
    if ($(idElemento).hasClass("is-invalid")){
        $(idElemento).removeClass("is-invalid");
    }
    $(idElemento).addClass("is-valid");
    if ($(feedback).hasClass("invalid-feedback")){
        $(feedback).removeClass("invalid-feedback");
    }
    $(feedback).addClass("valid-feedback");
    $(feedback).html("Campo válido");
}

/**
 * Función para indicar que el valor introducido en una entrada no es correcto
 * @param {string} idElemento El identificador del elementos input
 * @param {string} feedback El identificador del mensaje de error mostrado
 */
function showInvalido(idElemento,feedback){
    if ($(idElemento).hasClass("is-valid")){
        $(idElemento).removeClass("is-valid");
    }
    $(idElemento).addClass("is-invalid");
    if ($(feedback).hasClass("valid-feedback")){
        $(feedback).removeClass("valid-feedback");
    }
    $(feedback).addClass("invalid-feedback");
}

/**
 * Función para comprobar si una cadena coincide con el patrón de una expresión regular
 * @param {string} cadena El valor de la cadena
 * @param {string} regex El valor de la expresión regular
 * @return true en caso de que el valor coincida con el patrón de la expresión regular, false en caso contrario 
 */
function validarRegex(cadena,regex){
    if (regex.test(cadena) == true){
        //let res = cadena.match(regex);
        //console.log(res);
        return true;
    }
    return false;
}

/**
 * Método para comprobar si el formato del valor de una entrada valor coincide con el patrón de una expresión regular
 * @param {string} idInput El identificador de la entrada 
 * @param {string} feedbackInput El identificador del mensaje mostrado
 * @param {string} inputValue El valor de la entrada
 * @param {string} regex La expresión regular
 * @return true en caso de que el valor tenga un formato válido, false en caso contrario
 */
function validarFormato(idInput,feedbackInput,inputValue,regex){
    let valido = true;
    if (validarRegex(inputValue,regex)){
        showValido(idInput,feedbackInput);
    }
    else {
        showInvalido(idInput,feedbackInput);
        $(feedbackInput).html("El formato especificado no es correcto");
        valido = false;
    }
    return valido;
}

/**
 * Método para comprobar si un comentario tiene un formato válido, para ello se comprueba si cumple con el patrón de 
 * una expresión regular
 * @param {string} idInput El identificador del elemento input
 * @param {string} feedbackInput El identificador del mensaje mostrado
 * @param {string} inputValue El valor de la entrada
 * @param {string} regex La expresión regular
 * @return true en caso de que el valor tenga un formato válido, false en caso contrario
 */
function validarComentario(idInput,feedbackInput,inputValue,regex){
    let valido = true;
    if (inputValue.length == 0){
        showValido(idInput,feedbackInput);
    }
    else if (validarRegex(inputValue,regex)){
        showValido(idInput,feedbackInput);
    }
    else{
        showInvalido(idInput,feedbackInput);
        $(feedbackInput).html("El formato especificado no es correcto");
        valido = false;
    }
    return valido;
}

/**
 * Método para comprobar si el valor de una entrada es válido
 * @param {string} idInput El identificador del elemento input
 * @param {string} feedbackInput El identificador del mensaje mostrado
 * @param {string} inputValue El valor de la entrada
 * @param {string} regex La expresión regular
 * @return true en caso de que el valor sea válido, false en caso contrario
 */
function validarInput(idInput,feedbackInput,inputValue,regex){
    let valido = true;
    if (inputValue.length == 0){
        showInvalido(idInput,feedbackInput);
        $(feedbackInput).html("Este campo debe ser rellenado");
        valido = false;
    }
    else {
        valido = validarFormato(idInput,feedbackInput,inputValue,regex);
    }
    return valido;
}

/**
 * Método que comprueba si el valor de una entrada es válido y si tiene una longitud mínima
 * @param {string} idInput El identificador del elemento input
 * @param {string} feedbackInput El identificador del mensaje mostrado
 * @param {string} inputValue El valor de la entrada
 * @param {string} regex La expresión regular
 * @param {number} min La longitud mínima permitida
 * @return true en caso de que el valor de la entrada cumpla las condiciones, false en caso contrario
 */
function validarInput(idInput,feedbackInput,inputValue,regex,min){
    let valido = true;
    if (inputValue.length == 0){
        showInvalido(idInput,feedbackInput);
        $(feedbackInput).html("Este campo debe ser rellenado");
        valido = false;
    }
    else if (inputValue.length < min){
        showInvalido(idInput,feedbackInput);
        $(feedbackInput).html("Este campo no debe tener menos de "+ min +" caracteres");
        valido = false;
    }
    else {
        valido = validarFormato(idInput,feedbackInput,inputValue,regex);
    }
    return valido;
}

/**
 * Método para comprobar si el valor de la fecha introducida es válido
 * @param {string} idInputFecha El identificador del elemento input
 * @param {string} feedbackFecha El identificador del mensaje mostrado
 * @param {date} fecha El valor de la fecha
 * @return true en caso de que la fecha introducida sea válida, false en caso contrario
 */
function validarFecha(idInputFecha,feedbackFecha,fecha){
    console.log("Validar fecha: "+fecha);
    let valido = true;
    var currentDate = new Date();
    var inputDate = new Date(fecha);
    console.log("Input date: "+inputDate);
    if (inputDate < currentDate){
        showInvalido(idInputFecha,feedbackFecha);
        $(feedbackFecha).html("La fecha introducida es anterior a la fecha actual");
        valido = false;
    }
    else if (validarRegex(fecha,expFecha) == false){
        showInvalido(idInputFecha,feedbackFecha);
        $(feedbackFecha).html("El formato introducido no es correcto");
        valido = false;
    }
    else {
        showValido(idInputFecha,feedbackFecha);
    }
    return valido;
}

/**
 * Método para validar una entrada de tipo "Select"
 * @param {string} idTipo El identificador del elemento input
 * @param {string} feedbackTipo El identificador del mensaje mostrado
 * @param {string} tipo El valor introducido
 * @return true en caso de que el valor sea válido, false en caso contrario
 */
function validarSelect(idTipo,feedbackTipo,tipo){
    if (tipo == ""){
        showInvalido(idTipo,feedbackTipo);
        $(feedbackTipo).html("Este campo debe ser rellenado");
        return false;
    }
    else {
        showValido(idTipo,feedbackTipo);
    }
    return true;
}
/**
 * Método para validar una entrada de tipo number
 * @param {string} idNumber El identificador de la entrada
 * @param {string} feedbackNumber El identificador del mensaje mostrado
 * @param {number} number El valor del número introducido
 * @param {string} context El contexto del número introducido puede ser "minutos" o "estudiantes"
 * @return true en caso de que el número sea válido, false en caso contrario
 */
function validarInputNumber(idNumber,feedbackNumber,number,context){
    let valido = true;
    if (number == ""){
        showInvalido(idNumber,feedbackNumber);
        $(feedbackNumber).html("Este campo debe ser rellenado");
        valido = false;
    }
    else if (number <= 0){
        showInvalido(idNumber,feedbackNumber);
        $(feedbackNumber).html("El numero de "+ context +" no puede ser cero o negativo");
        valido = false;
    }
    else {
        showValido(idNumber,feedbackNumber);
    }
    return valido;
}

/**
 * Método para validar una entrada de tipo file
 * @param {*} idFile El identificador de la entrada
 * @param {*} feedbackFile El identificador del mensaje mostrado
 * @return true en caso de que haya un fichero seleccionado, false en caso contrario
 */
function validarInputFile(idFile,feedbackFile){
    console.log("Numero de ficheros seleccionados: "+$(idFile).prop("files").length);
    if ($(idFile).prop("files").length == 0){
        showInvalido(idFile,feedbackFile);
        $(feedbackFile).html("Este campo debe ser rellenado");
        return false;
    }
    else {
        showValido(idFile,feedbackFile);
    }
    return true;
}

/**
 * Método para validar los datos de un profesor
 * @param {Object} profesor El profesor a evaluar
 * @return true si los datos son correctos, false en caso contrario 
 */
function validarProfesor(profesor){

    var password = $("#userpass").val();
    var repeatpass = $("#repeatpass").val();

    var nombreValido = validarInput("#name","#feedback-name",profesor.nombre,expNombre);
    var apellidosValido = validarInput("#surnames","#feedback-surnames",profesor.apellidos,expApellidos);
    var usuarioValido = validarInput("#username","#feedback-username",profesor.usuario,expUsuario);
    var passValida = validarInput("#userpass","#feedback-userpass",profesor.contraseña,expPassword,8);
    var newPassValida = validarInput("#repeatpass","#feedback-repeatpass",repeatpass,expPassword,8);

    console.log("Repeat pass: "+repeatpass);
    
    if (password != repeatpass && password != "" && repeatpass != ""){
        showInvalido("#userpass","#feedback-userpass");
        showInvalido("#repeatpass","#feedback-repeatpass");
        $("#feedback-userpass").html("La contraseñas no coinciden");
        $("#feedback-repeatpass").html("La contraseñas no coinciden");
    }

    if (nombreValido && apellidosValido && usuarioValido && passValida && newPassValida){
        return true;
    }
    return false;
}

/**
 * Método para comprobar si los datos de una línea son válidos
 * @param {Object} linea La línea a evaluar
 * @return true en caso de que los datos sean válidos, false en caso contrario
 */

function validarLinea(linea){
    var tituloValido = validarInput("#tituloLinea","#feedback-titulo",linea.titulo,expTitulo,10);
    var descripcionValida = validarInput("#descripcion","#feedback-descripcion",linea.descripcion,expDescripcion,30);
    var validoEstudiantes = validarInputNumber("#number","#feedback-numero",linea.maxEstudiantes,"estudiantes");
    var validoTipo = validarSelect("#tipoLinea","#feedback-tipoLinea",linea.tipoLinea);
    if (tituloValido && descripcionValida && validoEstudiantes && validoTipo){
        return true;
    }
    return false;
}

/**
 * Método para comprobar si los datos de un estudiante son válidos
 * @param {Object} estudiante El estudiante a evaluar
 * @return true en caso de que los datos sean válidos, false en caso contrario
 */

function validarEstudiante(estudiante){
    var vocalesValido = true;
    var nombreValido = validarInput("#nombre","#feedback-nombre",estudiante.nombre,expNombre);
    var apellidosValido = validarInput("#apellidos","#feedback-apellidos",estudiante.apellidos,expApellidos);
    var urlValida = validarInput("#urlGit","#feedback-urlGit",estudiante.urlGit,expUrlGit);
    var fechaValida = validarFecha("#fechaEstimada","#feedback-fecha",estudiante.fechaEstimada);

    var estadoTrabajo = localStorage.getItem("estadoTrabajo");
    if (estadoTrabajo == "ENTREGADO"){
        vocalesValido = validarFormato("#vocales","#feedback-vocales",estudiante.vocales,expVocales);
    }
    if (nombreValido && apellidosValido && urlValida && fechaValida && vocalesValido){
        return true;
    }
    return false;
}

/**
 * Método para comprobar si los datos de una reunión son válidos
 * @param {Object} reunion La reunión a evaluar
 * @return true en caso de que los datos sean válidos, false en caso contrario
 */

function validarReunion(reunion,op){
    var memoriaValida = true;
    var tituloValido = validarInput("#titulo","#feedback-reunion",reunion.titulo,expTitulo,10);
    var tipoValido = validarSelect("#tipoReunion","#feedback-tipo",reunion.tipoReunion);
    var fechaValida = validarFecha("#fecha","#feedback-fecha",reunion.fecha);
    var duracionValida = validarInputNumber("#duracion","#feedback-duracion",reunion.minutos,"minutos");
    var comentarioValido = validarComentario("#comentario","#feedback-comentario",reunion.comentario,expDescripcion);
    if (op == "create"){
        memoriaValida = validarInputFile("#memoria","#feedback-memoria");
    }
    /* else if (op == "update"){
        memoriaValida = validarInput("#urlMemoria","#feedback-memoria",urlMemoria,expUrl);
    } */
    

    if (tituloValido && tipoValido && fechaValida && duracionValida && comentarioValido && memoriaValida){
        return true;
    }
    return false;
}

/**
 * Método para comprobar si los datos de calificación de un estudiante son válidos
 * @param {string} vocales Los vocales del tribunal 
 * @return true en caso de que los datos sean válidos, false en caso contrario
 */
function validarCalificacion(vocales){
    var validarVocales = validarInput("#vocales","#feedback-vocales",vocales,expVocales);
    var informeValido = validarInputFile("#informe","#feedback-informe");
    var memoriaValida = validarInputFile("#memoriaFinal","#feedback-memoriaFinal");

    if (validarVocales && informeValido && memoriaValida){
        return true;
    }
    return false;
}

$(function(){

    //Manejadores de evento para los campos de los formularios

    $("input[type='text'],input[type='number'],input[type='url'],textarea").keydown(function(){
        if ($(this).hasClass("is-invalid")){
            $(this).removeClass("is-invalid");
        }
        else if ($(this).hasClass("is-valid")){
            $(this).removeClass("is-valid");
        }
    });

    $("input[type='date']").click(function(){
        if ($(this).hasClass("is-invalid")){
            $(this).removeClass("is-invalid")
        }
        else if ($(this).hasClass("is-valid")){
            $(this).removeClass("is-valid");
        }
    });

    $("input[type='file']").focusin(function(){
        if ($(this).hasClass("is-invalid")){
            $(this).removeClass("is-invalid")
        }
        else if ($(this).hasClass("is-valid")){
            $(this).removeClass("is-valid");
        }
    });

   

    $("#tituloLinea").focusout(function(){
        //var res = $(this).val().match(regex);
        validarRegex($(this).val(),expTitulo);
        //console.log(res);
        console.log("La expresion regular es: "+expTitulo);
        console.log("El valor es "+$(this).val());
        console.log(expTitulo.test($(this).val()));
    });

    $("#nombre").focusout(function(){
        //var res = $(this).val().match(regex);
        validarRegex($(this).val(),expNombre);
        //console.log(res);
        console.log("La expresion regular es: "+expNombre);
        console.log("El valor es "+$(this).val());
        console.log(expNombre.test($(this).val()));
    })

    $("#fechaEstimada").focusout(function(){
        //var res = $(this).val().match(regex);
        validarRegex($(this).val(),expFecha);
        //console.log(res);
        console.log("La expresion regular es: "+expFecha);
        console.log("El valor es "+$(this).val());
        console.log(expFecha.test($(this).val()));
    })



});