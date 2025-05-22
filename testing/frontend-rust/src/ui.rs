use ratatui::{
    Frame,
    layout::{Constraint, Direction, Layout},
    widgets::{Block, Borders, Paragraph},
};

use crate::app::App;

// Rendering function
pub fn draw(f: &mut Frame, app: &App) {
    // Divide terminal in two blocks
    let chunks = Layout::default()
        .direction(Direction::Vertical)
        .margin(1)
        .constraints([Constraint::Length(3), Constraint::Min(0)])
        .split(f.area());

    // Widget for input
    let input = Paragraph::new(app.input.as_str())
        .block(Block::default().title("Entrada").borders(Borders::ALL));
    f.render_widget(input, chunks[0]);

    // Widget for output
    let output = Paragraph::new(app.output.as_str())
        .block(Block::default().title("Salida").borders(Borders::ALL));
    f.render_widget(output, chunks[1]);
}
