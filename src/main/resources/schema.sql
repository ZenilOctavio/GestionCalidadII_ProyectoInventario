CREATE TABLE IF NOT EXISTS usuarios(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT UNIQUE,
    password TEXT NOT NULL,
    fecha_hora_ultimo_inicio TEXT,
    rol TEXT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_usuario_name ON usuarios(nombre);

CREATE TABLE IF NOT EXISTS almacenes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL, ubicacion TEXT,
    fecha_hora_creacion TEXT,
    fecha_hora_ultima_modificacion TEXT,
    ultimo_usuario_en_modificar TEXT
);

CREATE TABLE IF NOT EXISTS productos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    precio REAL DEFAULT 0.0,
    cantidad INTEGER NOT NULL,
    departamento TEXT NOT NULL,
    almacen_id INTEGER,
    descripcion TEXT,
    fecha_hora_creacion INTEGER DEFAULT 0,
    fecha_hora_ultima_modificacion TEXT,
    ultimo_usuario_en_modificar TEXT,
    FOREIGN KEY (almacen_id) REFERENCES almacenes(id)
);