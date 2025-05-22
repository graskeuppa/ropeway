use std::{
    env,
    io::{BufReader, Read},
    path::PathBuf,
    process::{Command, Stdio},
};

/// Ejecuta el .jar de Java con un solo argumento y devuelve TODO el stdout
pub fn run_java_command(argument: &str) -> Result<String, String> {
    // Home
    let home: PathBuf = env::var("HOME")
        .map(PathBuf::from)
        .map_err(|e| format!("HOME is undefined, yo!: {}", e))?;

    // Complete route to the .jar file, added to home
    let mut jar_path = home.clone();
    jar_path.push("ropeway/testing/backend-java/app/build/libs/app-all.jar");

    // cwd :)
    let mut work_dir = home;
    work_dir.push("ropeway/testing");

    // Launch the backend
    let mut child = Command::new("java")
        .arg("-jar")
        .arg(&jar_path)
        .arg(argument)
        .current_dir(work_dir)
        .stdout(Stdio::piped())
        .spawn()
        .map_err(|e| format!("Somethin went wrong, yo!: {}", e))?;

    // Read the stdout
    let stdout = child.stdout.take().ok_or("No se pudo capturar stdout")?;
    let mut reader = BufReader::new(stdout);
    let mut output = String::new();
    reader
        .read_to_string(&mut output)
        .map_err(|e| format!("Error al leer salida: {}", e))?;

    Ok(output)
}
