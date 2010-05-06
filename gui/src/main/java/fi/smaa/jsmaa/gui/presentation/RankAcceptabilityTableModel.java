package fi.smaa.jsmaa.gui.presentation;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.simulator.SMAA2Results;

@SuppressWarnings("serial")
public class RankAcceptabilityTableModel extends SMAA2ResultsTableModel {

	public RankAcceptabilityTableModel(SMAA2Results results) {
		super(results);
	}

	public int getColumnCount() {
		return results.getAlternatives().size() + 1;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return results.getAlternatives().get(rowIndex);
		}
		Alternative alt = results.getAlternatives().get(rowIndex);
		return results.getRankAcceptabilities().get(alt).get(columnIndex-1);		
	}

	@Override
	public String getColumnName(int column) {
		if (column == 0) {
			return "Alternative";
		}
		return "Rank " + new Integer(column);
	}

}