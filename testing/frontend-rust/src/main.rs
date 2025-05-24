mod app;
mod commands;
mod ui;

use crate::app::App;
use crate::commands::{run_graph_command, run_java_command};
use crate::ui::draw;

use crossterm::{
    event::{self, DisableMouseCapture, EnableMouseCapture, Event, KeyCode, KeyModifiers},
    execute,
    terminal::{EnterAlternateScreen, LeaveAlternateScreen, disable_raw_mode, enable_raw_mode},
};
use ratatui::{Terminal, backend::CrosstermBackend};
use std::io;

// Entrance
fn main() -> Result<(), Box<dyn std::error::Error>> {
    // LET IT RIPPPP
    enable_raw_mode()?;

    // Change to alternate screen
    let mut stdout = io::stdout();
    execute!(stdout, EnterAlternateScreen, EnableMouseCapture)?;
    let backend = CrosstermBackend::new(stdout);
    let mut terminal = Terminal::new(backend)?;

    // Instantatiate the App
    let mut app = App::new();

    // Main loop
    let res = run_app(&mut terminal, &mut app);

    // Restore terminal to how it was before
    disable_raw_mode()?;
    execute!(
        terminal.backend_mut(),
        LeaveAlternateScreen,
        DisableMouseCapture
    )?;
    terminal.show_cursor()?;

    // Error handling
    if let Err(err) = res {
        eprintln!("Error: {}", err);
    }
    Ok(())
}

// Main loop (actual implementation, duh)
fn run_app(
    terminal: &mut Terminal<CrosstermBackend<std::io::Stdout>>,
    app: &mut App,
) -> io::Result<()> {
    loop {
        // Draw the frame to the terminal
        terminal.draw(|f| draw(f, app))?;
        if event::poll(std::time::Duration::from_millis(100))? {
            if let Event::Key(key) = event::read()? {
                match key.code {
                    // Ctrl + q to quit, arros to scroll up or down
                    KeyCode::Char('q') if key.modifiers.contains(KeyModifiers::CONTROL) => {
                        app.quit()
                    }
                    KeyCode::PageUp | KeyCode::Up => app.scroll_up(),
                    KeyCode::PageDown | KeyCode::Down => app.scroll_down(),
                    KeyCode::Enter => {
                        let input = app.input.trim();
                        // Special graph case
                        if input.starts_with("/graph ") {
                            let parts: Vec<&str> = input.split_whitespace().collect();
                            if parts.len() == 3 {
                                match run_graph_command(parts[1], parts[2]) {
                                    Ok(data) => app.load_graph(data),
                                    Err(e) => app.output = format!("Error graph: {}", e),
                                }
                            } else {
                                app.output = "{\"Command err\", Expected - GRAPH -d1- -d2-}".into();
                            }
                        // Only calls the backend if the imput isn't empty
                        } else if !input.is_empty() {
                            match run_java_command(input) {
                                Ok(o) => app.output = o,
                                Err(e) => app.output = format!("Error: {}", e),
                            }
                            app.scroll = 0;
                            app.show_chart = false;
                        }
                        app.input.clear(); // Flush the input once that's done
                    }
                    // Every character gets added to the input
                    KeyCode::Char(c) => app.input.push(c),
                    // ... except for the backspace, that baby erases the last character
                    KeyCode::Backspace => {
                        app.input.pop();
                    }
                    _ => {}
                }
            }
        }
        // Exits if the boolean turns true
        if app.should_quit {
            break;
        }
    }
    Ok(())
}
