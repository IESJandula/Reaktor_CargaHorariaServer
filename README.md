# Reaktor - Carga Horaria Server ⌛

Este es un proyecto de Spring Boot para la gestión de los horarios de los distintos departamentos, cursos, profesores, asignaturas y reducciones, permitiendo la gestión centralizada de la información relevante a través de archivos CSV.

## Endpoints🔌

## Departamentos

####  Subir departamentos (POST)📙
Sube un archivo CSV📝 con la información de los departamentos.

**Body:**
- `csv` (file): El archivo CSV📝 que contiene la información de los departamentos.

#### Obtener departamentos (GET)📗
Obtiene la información de todos los departamentos.

## Cursos

#### Subir cursos (POST)📙
Sube un archivo CSV📝 con la información de los cursos.

**Body:**
- `csv` (file): El archivo CSV📝 que contiene la información de los cursos.

####  Obtener cursos (GET)📗
Obtiene la información de todos los cursos.

## Profesores

#### Subir profesores (POST)📙
Sube un archivo CSV📝 con la información de los profesores.

**Body:**
- `csv` (file): El archivo CSV📝 que contiene la información de los profesores.

#### Obtener profesores (GET)📗
Obtiene la información de todos los profesores.

## Asignaturas

#### Subir asignaturas (POST)📙
Sube un archivo CSV📝 con la información de las asignaturas.

**Body:**
- `csv` (file): El archivo CSV📝 que contiene la información de las asignaturas.

#### Obtener asignaturas (GET)📗
Obtiene la información de todas las asignaturas.

#### Asignar asignatura (PUT)📘
Asigna la información de una asignatura específica a un profesor.

**Headers:**
- `IdProfesor`: Identificador del profesor.
- `nombreAsignatura`: Nombre de la asignatura.
- `curso`: Curso de la asignatura.
- `etapa`: Etapa educativa.
- `grupo`: Grupo de la asignatura.

#### Eliminar asignatura (DELETE)📕
Elimina la información de una asignatura específica a un profesor.

**Headers:**
- `IdProfesor`: Identificador del profesor.
- `nombreAsignatura`: Nombre de la asignatura.
- `curso`: Curso de la asignatura.
- `etapa`: Etapa educativa.
- `grupo`: Grupo de la asignatura.

## Guardias

#### Asignar guardias (POST)📙
Asigna horas de guardia a un profesor.

**Headers:**
- `idProfesor`: Identificador del profesor.
- `horasAsignadas`: Número de horas asignadas.

## Reducciones

#### Subir reducciones (POST)📙
Sube un archivo CSV📝 con la información de las reducciones de horas.

**Body:**
- `csv` (file): El archivo CSV📝 que contiene la información de las reducciones.

#### Obtener reducciones (GET)📗
Obtiene la información de todas las reducciones.

#### Asignar reducción (PUT)📘
Asigna la información de una reducción específica a un profesor.

**Headers:**
- `idProfesor`: Identificador del profesor.
- `idReduccion`: Identificador de la reducción.

#### Eliminar reducción (DELETE)📕
Elimina la información de una reducción específica asignada a un profesor.

**Headers:**
- `idProfesor`: Identificador del profesor.
- `idReduccion`: Identificador de la reducción.

## Resúmenes

#### Resumen de profesores (GET)📗
Obtiene el resumen de un profesor específico.

**Headers:**
- `idProfesor`: Identificador del profesor.

#### Resumen de departamentos (GET)📗
Obtiene el resumen de carga horaria de un departamento específico.

**Headers:**
- `nombreDepartamento`: Nombre del departamento.

## Matrículas

#### Subir matrícula de cursos (POST)📙
Sube un archivo CSV📝 con la información de la matrícula de cursos.

**Headers:**
- `curso`: Curso de la matrícula.
- `etapa`: Etapa educativa.

**Body:**
- `csv` (file): El archivo CSV📝 que contiene la información de la matrícula de cursos.

#### Obtener matrícula de cursos (GET)📗
Obtiene la información de la matrícula de todos los cursos.

## Bloques

#### Subir matrícula de bloques (POST)📙
Sube un archivo CSV📝 con la información de la matrícula de bloques.

**Headers:**
-  `curso`: Curso de la matrícula.
-  `etapa`: Etapa educativa.
- `nombreAsignatura`: Nombre de la asignatura.

#### Obtener matrícula de bloques (GET)📗
Obtiene la información de la matrícula de todos los bloques.

**Headers:**
- `curso`: Curso de la matrícula.
- `etapa`: Etapa educativa.

## Alumnos

#### Subir matrícula de alumnos (POST)📙
Sube la información de la matrícula de un alumno.

**Headers:**
- `alumno`: Nombre del alumno.
- `curso`: Curso de la matrícula.
- `etapa`: Etapa educativa.
- `grupo`: Grupo del alumno.

#### Obtener matrícula de alumnos (GET)📗
Obtiene la información de la matrícula de todos los alumnos.

**Headers:**
- `curso`: Curso de la matrícula.
- `etapa`: Etapa educativa.
- `grupo`: Grupo del alumno.

#### Eliminar matrícula de alumno (DELETE)📕
Elimina la información de la matrícula de un alumno.

**Headers:**
- `alumno`: Nombre del alumno.
- `curso`: Curso de la matrícula.
- `etapa`: Etapa educativa.
- `grupo`: Grupo del alumno.

## Resúmenes de Matrículas

#### Resumen de asignaturas (GET)📗
Obtiene el resumen de la matrícula de una asignatura específica.

**Headers:**
- `nombreAsignatura`: Nombre de la asignatura.

#### Resumen de cursos (GET)📗
Obtiene el resumen de la matrícula de un curso específico.

**Headers:**
- `curso`: Curso de la matrícula.
- `etapa`: Etapa educativa.
