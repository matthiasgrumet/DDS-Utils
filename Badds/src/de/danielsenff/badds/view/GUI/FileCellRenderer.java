/**
 * 
 */
package de.danielsenff.badds.view.GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.SystemColor;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableCellRenderer;

import model.DDSFile;


/**
 * @author danielsenff
 *
 */
public class FileCellRenderer extends JPanel implements ListCellRenderer,
		TableCellRenderer {

	JLabel filename = new JLabel();
	private JLabel fileicon;
	
	/* (non-Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	@Override
	public Component getListCellRendererComponent(
			JList list,          // the list being redrawn
			Object value,        // value to display
			int index,           // cell index
			boolean isSelected,  // is the cell selected
			boolean cellHasFocus)// the list and the cell have the focus
	{
		init(value, isSelected, cellHasFocus);
		
		return this;//return component used to render
	}

	private void init(Object value, boolean isSelected, boolean hasFocus) {
		
		// TODO selecting an element causes invalidation, and then the value is null
		
		
		File file = null;
		if (value instanceof DDSFile) {
			DDSFile image = (DDSFile) value;
			file = image.getFile();
			System.out.println("ddsfile");
		} else if (value instanceof File) {
			file = (File) value;
			System.out.println("file");
		} else if (value instanceof String) {
			file = new File((String) value);
			System.out.println("string");
		} else {
			System.out.println("hm something is wrong with the cellrenderer");
		}

		String theLabel = file.getName();
		Icon systemIcon = FileSystemView.getFileSystemView().getSystemIcon(file);
		fileicon = new JLabel(systemIcon);
		setLayout(new BorderLayout());
		filename.setText(theLabel);
		filename.setAlignmentX(Component.LEFT_ALIGNMENT);
		if (isSelected) { 
			setBackground(SystemColor.textHighlight);
		} else {
			setBackground(SystemColor.text);

		}//end else
		this.add(fileicon, BorderLayout.LINE_START);
		this.add(filename, BorderLayout.CENTER);
		
		
		
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(
			JTable table, 
			Object value,
			boolean isSelected, 
			boolean hasFocus, 
			int row, 
			int column) 
	{
		init(value, isSelected, hasFocus);
		return this;
	}

}
