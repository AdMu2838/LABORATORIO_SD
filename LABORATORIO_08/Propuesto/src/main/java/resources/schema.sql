-- Crear base de datos
CREATE DATABASE IF NOT EXISTS EmpresaDB;
USE EmpresaDB;

-- Tabla de Departamentos
CREATE TABLE Departamento (
    IDDpto INT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Telefono VARCHAR(15),
    Fax VARCHAR(15)
);

-- Tabla de Ingenieros
CREATE TABLE Ingeniero (
    IDIng INT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Especialidad VARCHAR(100),
    Cargo VARCHAR(50)
);

-- Tabla de Proyectos
CREATE TABLE Proyecto (
    IDProy INT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Fec_Inicio DATE NOT NULL,
    Fec_Termino DATE,
    IDIng INT,
    IDDpto INT,
    FOREIGN KEY (IDIng) REFERENCES Ingeniero(IDIng)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    FOREIGN KEY (IDDpto) REFERENCES Departamento(IDDpto)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT chk_fechas CHECK (Fec_Termino IS NULL OR Fec_Termino >= Fec_Inicio)
);

-- Índices secundarios para búsqueda rápida
CREATE INDEX idx_nombre_departamento ON Departamento(Nombre);
CREATE INDEX idx_nombre_proyecto ON Proyecto(Nombre);
CREATE INDEX idx_especialidad_ingeniero ON Ingeniero(Especialidad);

-- Inserción de nuevo departamento
PREPARE insertar_departamento FROM
'INSERT INTO Departamento (IDDpto, Nombre, Telefono, Fax) VALUES (?, ?, ?, ?)';

-- Inserción de nuevo ingeniero
PREPARE insertar_ingeniero FROM
'INSERT INTO Ingeniero (IDIng, Nombre, Especialidad, Cargo) VALUES (?, ?, ?, ?)';

-- Inserción de nuevo proyecto
PREPARE insertar_proyecto FROM
'INSERT INTO Proyecto (IDProy, Nombre, Fec_Inicio, Fec_Termino, IDIng, IDDpto) 
 VALUES (?, ?, ?, ?, ?, ?)';

-- Procedimiento para asignar un ingeniero a un proyecto
DELIMITER //
CREATE PROCEDURE AsignarIngeniero(
    IN p_IDProy INT,
    IN p_IDIng INT
)
BEGIN
    UPDATE Proyecto
    SET IDIng = p_IDIng
    WHERE IDProy = p_IDProy;
END;
//
DELIMITER ;

-- Procedimiento para listar proyectos de un departamento
DELIMITER //
CREATE PROCEDURE ListarProyectosPorDepartamento(
    IN p_IDDpto INT
)
BEGIN
    SELECT P.IDProy, P.Nombre, P.Fec_Inicio, P.Fec_Termino, I.Nombre AS Ingeniero
    FROM Proyecto P
    LEFT JOIN Ingeniero I ON P.IDIng = I.IDIng
    WHERE P.IDDpto = p_IDDpto;
END;
//
DELIMITER ;

START TRANSACTION;

-- Insertar departamentos de ejemplo
INSERT INTO Departamento VALUES (1, 'Ingeniería', '054-123456', '054-654321');
INSERT INTO Departamento VALUES (2, 'Desarrollo', '054-123457', '054-654322');
INSERT INTO Departamento VALUES (3, 'Sistemas', '054-123458', '054-654323');

-- Insertar ingenieros de ejemplo
INSERT INTO Ingeniero VALUES (101, 'Ana Torres', 'Sistemas', 'Jefe de Proyecto');
INSERT INTO Ingeniero VALUES (102, 'Carlos Mendez', 'Software', 'Desarrollador Senior');
INSERT INTO Ingeniero VALUES (103, 'Maria Rodriguez', 'Redes', 'Administrador de Red');
INSERT INTO Ingeniero VALUES (104, 'Luis Gonzalez', 'Base de Datos', 'DBA');

-- Insertar proyectos de ejemplo
INSERT INTO Proyecto VALUES (1001, 'Proyecto Alpha', '2025-01-01', '2025-12-31', 101, 1);
INSERT INTO Proyecto VALUES (1002, 'Sistema Web', '2025-02-01', '2025-08-31', 102, 2);
INSERT INTO Proyecto VALUES (1003, 'Infraestructura Red', '2025-03-01', NULL, 103, 3);
INSERT INTO Proyecto VALUES (1004, 'Migración BD', '2025-01-15', '2025-06-30', 104, 1);

COMMIT;
