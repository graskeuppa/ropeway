#[derive(Default)]

pub struct App {
    // Parameters
    pub input: String,
    pub output: String,
    pub scroll: u16,
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

If you´re unsure of what to do, run '/help' to see a list of all available commands. ",
            ),

            scroll: 0,
            should_quit: false,
        }
    }
    // Changing the bool
    pub fn quit(&mut self) {
        self.should_quit = true;
    }

    //Scrolling logic
    pub fn scroll_down(&mut self) {
        self.scroll = self.scroll.saturating_add(1);
    }
    pub fn scroll_up(&mut self) {
        if self.scroll > 0 {
            self.scroll -= 1;
        }
    }
}
