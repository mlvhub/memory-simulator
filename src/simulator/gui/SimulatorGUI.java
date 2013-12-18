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
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.omg.CORBA.FREE_MEM;

import simulator.Paginator;
import simulator.entity.Frame;
import simulator.entity.Page;
import simulator.entity.SimulatorFile;
import simulator.io.FileIO;
import simulator.util.AbstractGenerator;
import simulator.util.Generator;

public class SimulatorGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private final String SIMULATOR_TITLE = "Simulador de Memoria";

	private final AbstractGenerator fileIdGenerator = new Generator();

	private Paginator paginator;

	private JTable memoryCells;

	private JPanel pane;
	private JPanel simulatedMemoryContainer;
	private JPanel container;

	private JButton openFile;
	private JButton startSimulator;
	private JButton stopSimulator;
	private JLabel freeMemory;

	private List<SimulatorFile> loadedFiles = new ArrayList<SimulatorFile>();
	private JList loadedFilesList;
	private DefaultListModel loadedFilesModel = new DefaultListModel();

	private JTextField memoryAmountField;
	private JTextField frameSizeField;
	private JTextField sleepField;

	private SwingWorker worker;

	public SimulatorGUI() {

		setTitle(SIMULATOR_TITLE);

		container = new JPanel(new BorderLayout());
		int numberOfRows = 1;
		int numberOfColumns = 3;
		container.setLayout(new GridLayout(numberOfRows, numberOfColumns));

		simulatedMemoryContainer = new JPanel(new BorderLayout());

		pane = new JPanel(new BorderLayout());
		numberOfRows = 5;
		numberOfColumns = 2;
		pane.setLayout(new GridLayout(numberOfRows, numberOfColumns));
		container.add(pane);

		loadedFilesList = new JList(loadedFilesModel);
		loadedFilesList.setLayoutOrientation(JList.VERTICAL_WRAP);
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

		JLabel sleeplabel = new JLabel(" Tiempo entre cada página (ms): ");
		pane.add(sleeplabel);
		sleepField = new JTextField();
		pane.add(sleepField);

		JLabel freeMemoryLabel = new JLabel("Memoria libre:");
		pane.add(freeMemoryLabel);
		freeMemory = new JLabel("");
		pane.add(freeMemory);

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
		SimulatorFile simFile = new SimulatorFile(fileIdGenerator, file, chars,
				5000);
		loadedFiles.add(simFile);
		int index = loadedFilesModel.getSize() + 1;
		loadedFilesModel.addElement(simFile.getId() + ". " + simFile.getName());
		loadedFilesList = new JList(loadedFilesModel);
	}

	private boolean isFileLoaded(File file) {
		boolean isFileLoaded = false;
		for (SimulatorFile simFile : loadedFiles)
			if (simFile.getFile().getAbsolutePath()
					.equals(file.getAbsolutePath()))
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
			return frames[col][row].getFileId();
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

	class CustomRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component c = super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);

			MemoryTableModel model = (MemoryTableModel) table.getModel();
			if (model.isUsed(row, column))
				c.setBackground(Color.LIGHT_GRAY);
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
				if (!isFileLoaded(fileToLoad))
					loadFile(fileToLoad);
				else
					sendErrorMessage("¡El archivo ya está cargado!");
			}
		}
	}

	private void sendErrorMessage(String message) {
		String title = "ERROR";
		JOptionPane.showMessageDialog(getContentPane(), message, title,
				JOptionPane.ERROR_MESSAGE);
	}

	private class StartPaginator implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (loadedFiles.size() > 0) {
				openFile.setEnabled(false);
				pane.remove(startSimulator);
				pane.add(stopSimulator);
				pane.updateUI();

				int memoryAmount = getIntFromField(memoryAmountField);
				int frameAmount = getIntFromField(frameSizeField);
				final int sleep = getIntFromField(sleepField);
				paginator = new Paginator(memoryAmount, frameAmount,
						loadedFiles);
				initTable(paginator.getFrames());

				worker = new SwingWorker() {

					@Override
					protected Object doInBackground() throws Exception {
						System.out.println("doInBackground");
						System.out.println(loadedFiles.size());
						for (SimulatorFile file : loadedFiles) {
							List<Page> pagesPerFile = paginator.paginate(file);
							System.out.println(pagesPerFile.size());
							for (Page page : pagesPerFile) {
								paginator.loadPage(page);
								freeMemory.setText(String.valueOf(paginator
										.getFreeMemory()));
								initTable(paginator.getFrames());
								System.out.println(paginator.getFreeMemory());
								Thread.sleep(sleep);
							}
						}
						return null;
					}
				};
				worker.execute();
				System.out.println("executed");
			} else {
				sendErrorMessage("Debe cargar al menos un archivo.");
			}
		}

	}

	private class StopPaginator implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			openFile.setEnabled(true);
			pane.remove(stopSimulator);
			pane.add(startSimulator);
			pane.updateUI();

			paginator.stop();
			if (!worker.isDone())
				worker.cancel(true);

			for (SimulatorFile file : loadedFiles)
				file.renew();
		}

	}

	private int getIntFromField(JTextField field) {
		return Integer.parseInt(field.getText());
	}
}