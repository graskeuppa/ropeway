#[derive(Default)]

pub struct App {
    // Parameters
    pub input: String,
    pub output: String,
    pub should_quit: bool,
}

impl App {
    // Making a new App
    pub fn new() -> Self {
        // Constructor
        Self {
            input: String::new(),
            output: String::from(
                r"     
    ________   ____  ___ _      ______  __  __
   / ___/ __ \/ __ \/ _ \ | /| / / __ `/ / / /
  / /  / /_/ / /_/ /  __/ |/ |/ / /_/ / /_/ /
 /_/   \____/ .___/\___/|__/|__/\__,_/\__, /
           /_/                       /____/ (0.1.0-alpha.1)

Welcome to ropeway!

If you´re unsure of what to do, run '/help' to see a list of all available commands.

           ",
            ),
            should_quit: false,
        }
    }
    // Changing the bool
    pub fn quit(&mut self) {
        self.should_quit = true;
    }
}
