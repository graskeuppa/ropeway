use ratatui::{
    Frame,
    layout::{Constraint, Direction, Layout},
    //    style::Stylize,
    widgets::{self, Block, Borders, Paragraph, Wrap},
};

use crate::app::App;

// Draws the UI, prompt on the upper part, output below
pub fn draw(f: &mut Frame, app: &App) {
    // Divide terminal in two blocks
    let chunks = Layout::default()
        .direction(Direction::Vertical)
        .margin(1)
        .constraints([Constraint::Length(3), Constraint::Min(0)])
        .split(f.area());

    // Widget for input
    let input = Paragraph::new(app.input.as_str())
        .block(Block::default().title("Input").borders(Borders::ALL))
        .wrap(Wrap { trim: false });
    f.render_widget(input, chunks[0]);

    // Widget for output + added logic for scrolling when the output is too large
    let mut output = Paragraph::new(app.output.as_str())
        .block(Block::default().title("Output").borders(Borders::ALL))
        .wrap(Wrap { trim: false });
    output = output.scroll((app.scroll, 0));
    f.render_widget(output, chunks[1]);
}
