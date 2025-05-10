use serde::Deserialize;
use std::fs::File;
use std::io::BufReader;

#[derive(Debug, Deserialize)]
struct Persona {
    nombre: String,
    edad: u32,
}

fn main() -> Result<(), Box<dyn std::error::Error>> {
    //Abrir los archivos JSON generados por Java
    let file = File::open("../../../Java/serialization-tests/app/persona.json")?;
    let reader = BufReader::new(file);

    // Desealizar a struct Persona
    let persona: Persona = serde_json::from_reader(reader)?;

    //Mostrar contenido
    println!("Nombre: {}", persona.nombre);
    println!("Edad: {}", persona.edad);

    Ok(())
}
