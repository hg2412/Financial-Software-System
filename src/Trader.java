/**
 * Trader object
 *
 * @author Caroline Trimble, Kunal Jasty, Haoxiang Gao
 * @version 1 Build October 2015
 */

public class Trader {
	int id;
	double pnl;
	
	/** Constructor */
	public Trader(int id, double pnl) {
		this.id = id;
		this.pnl = pnl;
	}
	
	/** PnL accessor
	 * @return pnl double profit or loss
	 */
	public double getPnL() {
		return pnl;
	}
	
	/** Id accessor 
	 * @return id int trader id
	 */
	public int getId() {
		return id;
	}
}
