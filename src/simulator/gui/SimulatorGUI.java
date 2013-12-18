package simulator.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import simulator.Paginator;
import simulator.entity.Frame;
import simulator.entity.Page;
import simulator.entity.SimulatorFile;
import simulator.io.FileIO;
import simulator.util.Generator;

public class SimulatorGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private final String SIMULATOR_TITLE = "Simulador de Memoria";

	private Paginator paginator;
	
	private JTable memoryCells;
	
	private JPanel pane;
	private JPanel simulatedMemoryContainer;
	private JPanel container;

	private JButton openFile;
	private JButton startSimulator;
	private JButton stopSimulator;

	private List<SimulatorFile> loadedFiles = new ArrayList<SimulatorFile>();
	private JList loadedFilesList;
	private DefaultListModel loadedFilesModel = new DefaultListModel();

	private JTextField memoryAmountField;
	private JTextField frameSizeField;

	public SimulatorGUI() {

		setTitle(SIMULATOR_TITLE);

		container = new JPanel(new BorderLayout());
		int numberOfRows = 1;
		int numberOfColumns = 3;
		container.setLayout(new GridLayout(numberOfRows, numberOfColumns));

		simulatedMemoryContainer = new JPanel(new BorderLayout());

		pane = new JPanel(new BorderLayout());
		numberOfRows = 3;
		numberOfColumns = 2;
		pane.setLayout(new GridLayout(numberOfRows, numberOfColumns));
		container.add(pane);
		
		loadedFilesList = new JList(loadedFilesModel);
		loadedFilesList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		loadedFilesList.setVisibleRowCount(-1);
		container.add(loadedFilesList);

		JLabel firstNamelabel = new JLabel(" Cantidad de Memoria: ");
		pane.add(firstNamelabel);
		memoryAmountField = new JTextField();
		pane.add(memoryAmountField);

		JLabel frameSizelabel = new JLabel(" Tamaño de Marco: ");
		pane.add(frameSizelabel);
		frameSizeField = new JTextField();
		pane.add(frameSizeField);
		
		openFile = new JButton("Cargar Archivo");
		openFile.addActionListener(new OpenFile());
		pane.add(openFile);
		
		startSimulator = new JButton("Simular");
		startSimulator.addActionListener(new StartPaginator());
		pane.add(startSimulator);
		
		stopSimulator = new JButton("Detener");
		stopSimulator.addActionListener(new StopPaginator());

		// add the pane to the main window
		getContentPane().add(container);

		// Pack will make the size of window fitting to the compoents
		// You could also use for example setSize(300, 400);
		pack();
	}
	
	private void initTable(Frame[][] frames) {
		
		memoryCells = new JTable(new MemoryTableModel(frames));
		
		TableColumn column = null;
		for (int i = 0; i < memoryCells.getColumnModel().getColumnCount(); i++) {
		    column = memoryCells.getColumnModel().getColumn(i);
		    column.setPreferredWidth(5);
		}
		
		memoryCells.setDefaultRenderer(Frame.class, new CustomRenderer());
		simulatedMemoryContainer.add(memoryCells);
		container.add(simulatedMemoryContainer);
		pack();
	}
	
	private void loadFile(File file) {
		System.out.println(file.getName());
		List<Character> chars = FileIO.readCharacters(file);
		SimulatorFile simFile = new SimulatorFile(new Generator(), file, chars, 5000);
		loadedFiles.add(simFile);
		int index = loadedFilesModel.getSize() + 1;
		loadedFilesModel.addElement(index + ". " +simFile.getName());
		loadedFilesList = new JList(loadedFilesModel);
	}
	
	private boolean isFileLoaded(File file) {
		boolean isFileLoaded = false;
		for(SimulatorFile simFile : loadedFiles)
			if(simFile.getFile().getAbsolutePath().equals(file.getAbsolutePath()))
				return true;
		return isFileLoaded;
	}
	
	class MemoryTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		
		private Frame[][] frames;
		
		public MemoryTableModel(Frame[][] frames) {
			this.frames = frames;
		}

	    public int getColumnCount() {
	        return frames.length;
	    }

	    public int getRowCount() {
	        return frames[0].length;
	    }

	    public Object getValueAt(int row, int col) {
	        return "";
	    }

	    public Class getColumnClass(int c) {
	        return frames[c][0].getClass();
	    }
	    
	    public void setValueAt(Frame frame, int row, int col) {
	        frames[col][row] = frame;
	        fireTableCellUpdated(row, col);
	    }
	    
	    public boolean isUsed(int row, int col) {
	    	return frames[col][row].isUsed();
	    }
	}
	
	class CustomRenderer extends DefaultTableCellRenderer 
	{
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	    {
	        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	        MemoryTableModel model = (MemoryTableModel) table.getModel();
	        if(model.isUsed(row, column))
	        	c.setBackground(Color.RED);
	        else
	        	c.setBackground(Color.WHITE);

	        return c;
	    }
	}

	class OpenFile implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
			int rVal = c.showOpenDialog(SimulatorGUI.this);

			if (rVal == JFileChooser.APPROVE_OPTION) {
				File fileToLoad = c.getSelectedFile();
				if(!isFileLoaded(fileToLoad))
					loadFile(fileToLoad);
				else
					sendErrorMessage("¡El archivo ya está cargado!");
			}
		}
	}
	
	private void sendErrorMessage(String message) {
		String title = "ERROR";
		JOptionPane.showMessageDialog(getContentPane(), message, title, JOptionPane.ERROR_MESSAGE);
	}

	private class StartPaginator implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			openFile.setEnabled(false);
			pane.remove(startSimulator);
			pane.add(stopSimulator);
			pane.updateUI();
			
			int memoryAmount = getIntFromField(memoryAmountField);
			int frameAmount = getIntFromField(frameSizeField);
			paginator = new Paginator(memoryAmount, frameAmount, loadedFiles);
			paginator.run();
			initTable(paginator.getFrames());
			JOptionPane.showMessageDialog(getContentPane(), "Memoria libre "+paginator.getFreeMemory()+" bytes");
		}

	}
	
	private class StopPaginator implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			openFile.setEnabled(true);
			pane.remove(stopSimulator);
			pane.add(startSimulator);
			pane.updateUI();
			
			paginator.stop();
		}

	}

	private int getIntFromField(JTextField field) {
		// TODO Ver si manejamos valores invalidos
		return Integer.parseInt(field.getText());
	}
}