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
            output: String::new(),
            should_quit: false,
        }
    }
    // Changing the bool
    pub fn quit(&mut self) {
        self.should_quit = true;
    }
}
