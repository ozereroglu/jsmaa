/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013.

    JSMAA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSMAA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSMAA.  If not, see <http://www.gnu.org/licenses/>.
*/
package fi.smaa.jsmaa.gui.views;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.drugis.common.gui.ViewBuilder;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.jsmaa.gui.presentation.PreferencePresentationModel;
import fi.smaa.jsmaa.gui.presentation.PreferencePresentationModel.PreferenceType;
import fi.smaa.jsmaa.model.CardinalPreferenceInformation;
import fi.smaa.jsmaa.model.OrdinalPreferenceInformation;
import fi.smaa.jsmaa.model.SMAAModel;

public class PreferenceInformationView implements ViewBuilder {
	private PreferencePresentationModel model;
	private JComponent prefPanel;
	private ScaleRenderer renderer;
	
	public PreferenceInformationView(PreferencePresentationModel model) {
		this.model = model;
		this.renderer = new DefaultScaleRenderer();
	}
	
	public PreferenceInformationView(PreferencePresentationModel model, ScaleRenderer renderer) {
		this.model = model;
		this.renderer = renderer;
	}	

	@SuppressWarnings("serial")
	public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"right:pref, 3dlu, left:pref:grow",
				"p, 3dlu, p");
		
		int fullWidth = 3;
		
		ValueModel preferenceTypeModel = model.getModel(PreferencePresentationModel.PREFERENCE_TYPE);

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setBorder(BorderFactory.createEmptyBorder());		
		CellConstraints cc = new CellConstraints();
		
		PreferenceType[] valueList = null;
		if (model.includesCardinalPreferences()) {
			valueList = PreferenceType.values();			
		} else {
			valueList = new PreferenceType[]{PreferenceType.MISSING, PreferenceType.ORDINAL};			
		}
		SelectionInList<PreferenceType> typeSelInList 
			= new SelectionInList<PreferenceType>(valueList, preferenceTypeModel);
		

		JComboBox preferenceTypeBox = BasicComponentFactory.createComboBox(typeSelInList);
		builder.add(preferenceTypeBox, cc.xy(1, 1));

		builder.addLabel("Preference information", cc.xyw(3, 1, fullWidth-2));

		prefPanel = null;
		if (model.getPreferenceType() == PreferenceType.ORDINAL) {
			SMAAModel smodel = model.getBean();
			OrdinalPreferencesView oview = new OrdinalPreferencesView((OrdinalPreferenceInformation) smodel.getPreferenceInformation());
			oview.setScaleRenderer(renderer);
			prefPanel = oview.buildPanel();
		} else if (model.getPreferenceType() == PreferenceType.CARDINAL) {
			CardinalPreferencesView oview = new CardinalPreferencesView(
					(CardinalPreferenceInformation) model.getBean().getPreferenceInformation());
			oview.setScaleRenderer(renderer);			
			prefPanel = oview.buildPanel();
		}
		if (prefPanel != null) {
			builder.add(prefPanel, cc.xyw(1, 3, fullWidth));
		}
		
		preferenceTypeBox.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (prefPanel != null) {
					prefPanel.requestFocusInWindow();
				}
			}
		});
		
		return builder.getPanel();
	}
}
