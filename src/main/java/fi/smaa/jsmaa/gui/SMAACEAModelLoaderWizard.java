package fi.smaa.jsmaa.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.models.StaticModel;

import au.com.bytecode.opencsv.CSVReader;

import com.jidesoft.swing.JideButton;

import fi.smaa.common.gui.ImageLoader;
import fi.smaa.jsmaa.ModelFileManager;
import fi.smaa.jsmaa.gui.presentation.SMAACEADataImportTM;
import fi.smaa.jsmaa.model.SMAACEAModel;
import fi.smaa.jsmaa.model.cea.InvalidInputException;
import fi.smaa.jsmaa.model.cea.SMAACEAModelImporter;

public class SMAACEAModelLoaderWizard {

	private ModelFileManager mgr;
	private JFrame parent;
	private StaticModel wizardModel;
	private SMAACEAModelImporter importData;	

	public SMAACEAModelLoaderWizard(ModelFileManager mgr) {
		this.mgr = mgr;
	}

	public void start(JFrame parent) {
		this.parent = parent;
		wizardModel = new StaticModel();

		wizardModel.add(new ChooseFileStep());
		wizardModel.add(new SelectColumnsStep());

		Wizard wizard = new Wizard(wizardModel);
		wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);
		wizard.setPreferredSize(new Dimension(800, 600));
		wizard.showInDialog("Import SMAA-CEA model", parent, true);
	}

	@SuppressWarnings("serial")
	private class ChooseFileStep extends PanelWizardStep {
		public ChooseFileStep() {
			super("Choose file", "Choose the CSV file to import SMAA-CEA model from");
			setLayout(new FlowLayout());
			final JFileChooser fs = new JFileChooser(new File("."));
			JideButton openButton = new JideButton("Choose file", ImageLoader.getIcon(FileNames.ICON_OPENFILE));
			final JTextField field = new JTextField("no file selected");
			field.setColumns(20);
			field.setEnabled(false);	
			openButton.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int retval = fs.showOpenDialog(parent);
					if (retval == JFileChooser.APPROVE_OPTION) {
						File file = fs.getSelectedFile();
						loadFile(field, file);
						if (wizardModel.isNextAvailable()) {
							wizardModel.nextStep();
						}
					}
				}

				private void loadFile(final JTextField field, File file) {
					try {
						CSVReader reader = new CSVReader(new FileReader(file), ' ');
						importData = new SMAACEAModelImporter(reader.readAll());
						field.setText(file.getName());
						setComplete(file != null);
					}  catch (InvalidInputException e) {
						JOptionPane.showMessageDialog(parent, e.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
						setComplete(false);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(parent, e.getMessage(), "Error loading file", JOptionPane.ERROR_MESSAGE);
						setComplete(false);
					}
				}				
			});
			add(openButton);
			add(field);
		}
	}

	@SuppressWarnings("serial")
	private class SelectColumnsStep extends PanelWizardStep {
		private JScrollPane spane;

		public SelectColumnsStep() {
			super("Tag columns", "Select columns to use as patient ID, treatment ID, cost, efficacy, and possible censoring inputs");
			spane = new JScrollPane();
			add(spane);
		}

		@Override
		public void prepare() {
			SMAACEADataImportTM tableModel = new SMAACEADataImportTM(importData);
			setComplete(importData.isComplete());			
			tableModel.addTableModelListener(new TableModelListener() {
				@Override
				public void tableChanged(TableModelEvent arg0) {
					setComplete(importData.isComplete());
				}				
			});
			JTable table = new JTable(tableModel);

			table.setCellSelectionEnabled(false);

			for (int i=0;i<table.getColumnModel().getColumnCount();i++) {
				TableColumn col = table.getColumnModel().getColumn(i);
				SMAACEAImportDataCellRenderer renderer = new SMAACEAImportDataCellRenderer();
				col.setCellRenderer(renderer);
				col.setCellEditor(new DefaultCellEditor(renderer));
			}
			spane.setViewportView(table);
		}
		
		@Override
		public void applyState() {
			SMAACEAModel model = importData.constructModel();
			mgr.setModel(model);
		}
	}
}
