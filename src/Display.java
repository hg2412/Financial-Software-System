
/**
 * Display Class creates the GUI for the Trade Capture System
 *
 * @author Caroline Trimble, Kunal Jasty, Haoxiang Gao
 * @version 1 Build October 2015
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.GridLayout;
import javax.swing.*;

import com.sun.media.sound.Toolkit;

import quickfix.ConfigError;

public class Display implements Runnable {

	/** Run display in new thread */
	public void run() {

		// DatabaseManager used to query MySQL database
		final DatabaseManager db = DatabaseManager.getInstance();

		JButton button = new JButton();
		// Generate CSV trade or aggregate reports
		button.setText("Generate a Report");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane
						.showOptionDialog(null, "What type of report would you like to produce?", "Feedback",
								JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[] {
										"CSV of Trades Entered", "CSV Showing Aggregate Positions", "PnL Report" },
								"default");

				// CSV of trades entered
				if (result == 0) {
					// New save dialog object created; displays window
					// for user to pick a directory to save CSV report
					SaveDialog s = new SaveDialog();
					// Saves the filename
					String filename = s.getName();

					System.out.println("CSV of Trades Entered");
					db.outputTrades(filename);
				}

				// CSV of aggregate positions
				if (result == 1) {
					// New save dialog object created; displays window
					// for user to pick a directory to save CSV report
					SaveDialog s = new SaveDialog();

					// Saves the filename
					String filename = s.getName();

					System.out.println("CSV Showing Aggregate Positions");
					db.outputAggregate(filename);
				}
				if (result == 2) {
					// New save dialog object created; displays window
					// for using to pick a directory to save PnL report

					SaveDialog s = new SaveDialog();

					String filename = s.getName();

					System.out.println("PnL Report");
					db.outputPnL(filename);
				}
			}
		});

		// CME commodity symbol
		JTextField symbol = new JTextField();

		// Month of expiry
		Integer[] months = { 1, 2, 3, 4, 5, 6, 7, 8, 8, 10, 11, 12 };
		JComboBox<Integer> month = new JComboBox<Integer>(months);

		// Year of expiry
		Integer[] years = { 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025, 2026 };
		JComboBox<Integer> year = new JComboBox<Integer>(years);

		// # of lots and price
		JTextField lots = new JTextField();
		JTextField price = new JTextField();

		// Buy or Sell
		String[] items = { "Buy", "Sell" };
		JComboBox<String> buySell = new JComboBox<String>(items);

		// Type of Order
		String[] orders = { "Market", "Limit", "Pegged" };
		JComboBox<String> orderT = new JComboBox<String>(orders);

		// Each trader will be assigned a unique id
		JTextField trader = new JTextField();

		// Create fields and comboboxes
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(button);
		panel.add(new JLabel("Symbol (e.g. AA):"));
		panel.add(symbol);
		panel.add(new JLabel("Contract Expiry Month:"));
		panel.add(month);
		panel.add(new JLabel("Contract Expiry Year:"));
		panel.add(year);
		panel.add(new JLabel("Lots:"));
		panel.add(lots);
		panel.add(new JLabel("Price(Limit Order) or Offset(Pegged Order):"));
		panel.add(price);
		panel.add(buySell);
		panel.add(new JLabel("Order Type:"));
		panel.add(orderT);
		panel.add(new JLabel("Trader"));
		panel.add(trader);
		// Adds a label to each and then adds to panel

		int result = JOptionPane.showConfirmDialog(null, panel, "Trade Capture System", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		// Users can enter multiple trades
		while (result != JOptionPane.CANCEL_OPTION) {
			if (result == JOptionPane.OK_OPTION) {

				// Ensure that all fields are completed
				if (symbol.getText().equals("") || lots.getText().equals("") || trader.getText().equals("")) {
					JOptionPane.showMessageDialog(panel, "Please enter data into all fields.");
					result = JOptionPane.showConfirmDialog(null, panel, "Trade Capture System",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

				} else {

					// int l for lots and double p for price
					int l = 0;
					double p = 0.0;

					// Type checking
					try {
						l = Integer.parseInt(lots.getText());
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(panel, "Please enter an integer for lots.");
					}

					try {
						db.getMarketPrice(symbol.getText());
					} catch (SQLException e2) {
						JOptionPane.showMessageDialog(panel, "Wrong symbol!");
					}
					
					if (orderT.getSelectedItem().equals("Market") == true){
						p = 0;
					}else{
						try {
							p = Double.parseDouble(price.getText());
						} catch (NumberFormatException e) {
							JOptionPane.showMessageDialog(panel, "Please enter number for price.");
						}
					}

					int b = ((String) buySell.getSelectedItem()).equals("Buy") ? 1 : -1;
					int ot;
					if (((String) orderT.getSelectedItem()).equals("Market") == true) {
						ot = 0;
					} else if (((String) orderT.getSelectedItem()).equals("Limit") == true)
						ot = 1;
					else
						ot = 2;
					// Create a new order and insert into DB
					Order o = new Order(symbol.getText(), (Integer) month.getSelectedItem(),
							(Integer) year.getSelectedItem(), l, p, b, ot, Integer.parseInt(trader.getText()));
					o.printOrder();
					try {
						o.sendOrdertoExchange();
					} catch (Throwable e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					// Resets GUI
					symbol.setText("");
					lots.setText("");
					price.setText("");
					trader.setText("");
					result = JOptionPane.showConfirmDialog(null, panel, "Trade Capture System",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				}
			}
		}

	}

}
