use ratatui::{
    Frame,
    layout::{Constraint, Direction, Layout},
    // style::{Color, Style},
    widgets::{BarChart, Block, Borders, Paragraph, Wrap},
    // widgets::{self, Axis, Dataset}
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

    // Widget for output,

    if app.show_chart && !app.graph_data.is_empty() {
        let mut items: Vec<(&'static str, u64)> = Vec::new();
        for (i, &(_x, y)) in app.graph_data.iter().enumerate() {
            let label = Box::leak((i + 1).to_string().into_boxed_str());
            items.push((label, y as u64));
        }

        // Computes the max to scale the bars
        let max = app
            .graph_data
            .iter()
            .map(|&(_x, y)| y as u64)
            .max()
            .unwrap_or(0);

        let barchart = BarChart::default()
            .block(Block::default().title("Gráfico").borders(Borders::ALL))
            .data(&items)
            .bar_width(5)
            .bar_gap(2)
            .max(max);

        f.render_widget(barchart, chunks[1]);
    } else {
        let mut output = Paragraph::new(app.output.as_str())
            .block(Block::default().title("Output").borders(Borders::ALL))
            .wrap(Wrap { trim: false });
        output = output.scroll((app.scroll, 0));
        f.render_widget(output, chunks[1]);
    }
}
