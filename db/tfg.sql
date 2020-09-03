-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 20-06-2017 a las 11:35:57
-- Versión del servidor: 5.7.14
-- Versión de PHP: 5.6.25

drop database if exists tfg;
create database tfg;
use tfg;

/* SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00"; */

CREATE TABLE `profesor` (
  `id` int(11) UNSIGNED NOT NULL auto_increment,
  `nombre` varchar(255) NOT NULL,
  `apellidos` varchar(255) NOT NULL,
  `usuario` varchar(255) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
   primary key(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `linea` (
  `id` int(11) UNSIGNED NOT NULL auto_increment,
  `titulo` varchar(255) NOT NULL,
  `descripcion` varchar(255) NOT NULL,
  `tipoLinea` varchar(255) NOT NULL,
  `maxEstudiantes` int(11) NOT NULL,
  `profesor` int(11) UNSIGNED,
   primary key(`id`),
   key `profesor` (`profesor`),
   foreign key(`profesor`) references profesor(`id`) on delete cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `estudiante` (
  `id` int(11) UNSIGNED NOT NULL auto_increment,
  `nombre` varchar(255) default null,
  `apellidos` varchar(255) default null,
  `urlGit` varchar(255) default null,
  `fechaEstimada` date default null,
  `estadoTrabajo` varchar(255) default null,
  `urlInforme` varchar(255) default null,
  `urlMemoriaFinal` varchar(255) default null,
  `vocales` varchar(255) default null,
  `linea` int(11) UNSIGNED,
   primary key(`id`),
   key `linea` (`linea`),
   foreign key(`linea`) references linea(`id`) on delete cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `reunion` (
  `id` int(11) UNSIGNED NOT NULL auto_increment,
  `titulo` varchar(255) NOT NULL,
  `fecha` date NOT NULL,
  `minutos` int(11) NOT NULL,
  `tipoReunion` varchar(255) NOT NULL,
  `urlMemoria` varchar(255) default null,
  `comentario` varchar(255) default null,
  `estudiante` int(11) UNSIGNED,
   primary key(`id`),
   key `estudiante` (`estudiante`),
   foreign key(`estudiante`) references estudiante(`id`) on delete cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/* INSERT INTO `profesor` (`id`, `nombre`, `apellidos`, `usuario`, `contraseña`) VALUES
(1, 'Jorge', 'Lopez Saura', 'jorge-ls', 'jorgels'); */
