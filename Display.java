/** Display Class creates the GUI for the Trade Capture System
*
* @author Caroline Trimble
* @version 1 Build October 2015
*/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import javax.swing.*;
import java.util.Calendar; 
import java.text.DateFormat; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Display {
	
	public static void main(String[] args) {
		JButton button = new JButton(); 
		button.setText("Generate a Report"); 
		//Creates a button to generate a report from the database 
		button.addActionListener(
		//The action performed if the user clicks the button to generate report:
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					int result = JOptionPane.showOptionDialog(null, 
        				"What type of report would you like to produce?", 
        				"Feedback", 
        				JOptionPane.OK_CANCEL_OPTION, 
        				JOptionPane.INFORMATION_MESSAGE, 
        				null, 
        				new String[]{"CSV of Trades Entered", "CSV Showing Aggregate Positions"}, // this is the array
        				"default");
					//A new window pops up with two options (for now)
					if(result == 0){
					//If the user clicks "CSV of Trades Entered" 
						System.out.println("CSV of Trades Entered");
						//For now just print -- later code to produce report
					}
					if(result == 1){
					//If the user clicks "CSV Showing Aggregate Positions"
						System.out.println("CSV Showing Aggregate Positions");			
					}
					}
				}
			);
		JTextField symbol = new JTextField();
		JTextField contractEx = new JTextField();
		JTextField lots = new JTextField();
		JTextField price = new JTextField();
		String[] items = {"Buy", "Sell"};
		JComboBox<String> combo = new JComboBox<String>(items);
		JTextField trader = new JTextField();
		//Creates text fields and one combo box for the parameters of order
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(button);
		panel.add(new JLabel("Symbol (e.g. HH):"));
		panel.add(symbol);
		panel.add(new JLabel("Contract Expiry (e.g. JUL 16):"));
		panel.add(contractEx);
		panel.add(new JLabel("Lots:"));
		panel.add(lots);
		panel.add(new JLabel("Price:"));
		panel.add(price);
		panel.add(combo);
		panel.add(new JLabel("Trader"));
		panel.add(trader);
		//Adds a label to each and then adds to panel

		int result = JOptionPane.showConfirmDialog(null, panel, "Trade Capture System",
        	JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		//Two buttons are displayed "Ok" and "Cancel" 
    		while(result != JOptionPane.CANCEL_OPTION){
    			if (result == JOptionPane.OK_OPTION) {
			//If the user clicks ok: 
        			if(symbol.getText().equals("") || 
					contractEx.getText().equals("") || 
					lots.getText().equals("") || 
					price.getText().equals("") || 
					trader.getText().equals("")){
            			JOptionPane.showMessageDialog(panel, "Please enter data into all fields.");
            			result = JOptionPane.showConfirmDialog(null, panel, "Trade Capture   System",
            			JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				//Checks if user has filled in all boxes, if not displays a message asking
				//the user to fill in every box
        			}
        			else{	
        				int l = 0; 
					double p = 0.0; 
					//int l for lots and double p for price
					try {
   						 l = Integer.parseInt(lots.getText());
  					} catch (NumberFormatException e) {
    					JOptionPane.showMessageDialog(panel, "Please enter an integer for lots."); 
  					}
					//Tries to parse the String lots the user entered into a int
					//If unsuccessful notifies user to enter an integer
					try{
						p = Double.parseDouble(price.getText());
					} catch(NumberFormatException e){
						JOptionPane.showMessageDialog(panel, "Please enter number for price.");
					}
					//Tries to parse the String price into a double; notifies if error
					Order o = new Order(symbol.getText(), contractEx.getText(), l, p, (String)combo.getSelectedItem(), trader.getText());
					//Creates a new order with the information user entered into GUI 
					o.printOrder(); 
					//For now, prints order info 

        				symbol.setText(""); 
    					contractEx.setText("");
    					lots.setText("");
    					price.setText(""); 
    					trader.setText("");
					//Resets text boxes to empty
        				result = JOptionPane.showConfirmDialog(null, panel, "Trade Capture System",
        				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					//Reshows the whole screen so the user can enter multiple trades 
    				}
    			}

		}
	}
}

