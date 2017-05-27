package vutils.objects;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

// TODO Javadoc

public abstract class SheetTable extends JTable {
	
	private ArrayList<Object> items;
	private String[] columns;
	
	private AbstractTableModel tableModel;
	private JScrollPane scrollableTable = null;
	
	public SheetTable(ArrayList<Object> items, String[] columns){
		
		super();
		
		this.items = items;
		this.columns = columns;
		
		this.tableModel = new AbstractTableModel(){
			
			@Override
			public Object getValueAt(int rowIndex, int columnIndex){
				return getValueAt(rowIndex, columnIndex);
			}
			
			@Override
			public int getColumnCount(){
				return SheetTable.this.columns.length;
			}
			
			@Override
			public int getRowCount(){
				return SheetTable.this.items.size();
			}
			
			@Override
			public String getColumnName(int column){
				return SheetTable.this.columns[column];
			}
			
		};
		
		if(items == null){
			
			DefaultTableModel emptyModel = new DefaultTableModel(new Object[][]
			{
				{
					null, null
				}
			}, columns);
			
			setModel(emptyModel);
			
		}
		else{
			
			setModel(tableModel);
			
		}
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		getSelectionModel().addListSelectionListener(
				new ListSelectionListener(){
					@Override
					public void valueChanged(ListSelectionEvent e){
						if(getRowCount() != 0)
							actionOnSelect(true);
					}
				});
		
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				if(e.getClickCount() == 2){
					actionOnDoubleClick(getSelectedItem());
				}
			}
		});
		
	}
	
	@Override
	public abstract Object getValueAt(int rowIndex, int columnIndex);
	
	protected void actionOnSelect(boolean hasSelection){}
	
	protected void actionOnDoubleClick(Object doubleClickedObject){}
	
	@Override
	public Class<?> getColumnClass(int columnIndex){
		return super.getColumnClass(columnIndex);
	}
	
	@Override
	public String getColumnName(int columnIndex){
		return columns[columnIndex];
	}
	
	public String[] getColumns(){
		return columns;
	}
	
	public void addItem(Object newItem){
		
		items.add(newItem);
		tableModel.fireTableRowsInserted(items.size() - 1, items.size() - 1);
		
	}
	
	public void modifyItemAt(int index, Object modifiedObject){
		
		items.set(index, modifiedObject);
		tableModel.fireTableRowsUpdated(index, index);
		
	}
	
	public void removeItem(int rows){
		
		items.remove(rows);
		tableModel.fireTableRowsDeleted(rows, rows);
		
	}
	
	public Object getSelectedItem(){
		return getItem(getSelectedRow());
	}
	
	public Object getItem(int index){
		
		if(index >= 0 && index < items.size())
			return items.get(index);
		else
			return null;
		
	}
	
	public void setSelectedItem(int index){
		
		if(getRowCount() != 0){
			setRowSelectionInterval(index, index);
			requestFocus();
		}
		else{
			actionOnSelect(false);
		}
		
	}
	
	public void setObjects(ArrayList<Object> items){
		
		if(items.size() > 0){
			this.items = items;
			fireTableDataChanged();
			setSelectedItem(0);
		}
		else{
			this.items.clear();
			fireTableDataChanged();
		}
		
	}
	
	public void fireTableDataChanged(){
		tableModel.fireTableDataChanged();
	}
	
	@Override
	public TableModel getModel(){
		return tableModel;
	}
	
	/**
	 * Essentially does what {@link javax.swing.JTable#getModel()
	 * <i>getModel()</i>} does, except by returning the model as an
	 * AbstractTableModel directly.
	 */
	public AbstractTableModel getAbstractModel(){
		return tableModel;
	}
	
	public JScrollPane getScrollableTable(){
		if(scrollableTable == null)
			scrollableTable = new JScrollPane(this);
		
		return scrollableTable;
	}
	
}
