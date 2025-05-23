use std::{
    env,
    io::{BufReader, Read, Write},
    path::PathBuf,
    process::{Command, Stdio},
};

use serde_json::Value;

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
        .stdin(Stdio::piped())
        .stdout(Stdio::piped())
        .current_dir(work_dir)
        .spawn()
        .map_err(|e| format!("Something went wrong, yo!: {}", e))?;

    //  Write the command and a new line to Java through the stdin
    if let Some(mut stdin) = child.stdin.take() {
        writeln!(stdin, "{}", argument)
            .map_err(|e| format!("Something went wrong with writing the stdin, yo!: {}", e))?;
    }

    // Read the stdout
    let stdout = child.stdout.take().ok_or("Couldn't get the stdout, yo!")?;
    let mut reader = BufReader::new(stdout);
    let mut output_raw = String::new();
    reader
        .read_to_string(&mut output_raw)
        .map_err(|e| format!("Could'nt read the output, yo!: {}", e))?;

    // Wait for the child to finish
    let _ = child.wait();

    // Make the JSON pretty!
    if let Ok(json) = serde_json::from_str::<Value>(&output_raw) {
        serde_json::to_string_pretty(&json)
            .map_err(|e| format!("Couldn't format to JSON, yo!: {}", e))
    } else {
        Ok(output_raw)
    }
}
