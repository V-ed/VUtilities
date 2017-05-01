package objects;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public abstract class SheetTable extends JTable {
	
	private ArrayList<TableObject> items;
	private String[] columns;
	
	private AbstractTableModel tableModel;
	private JScrollPane scrollableTable = null;
	
	public SheetTable(ArrayList<TableObject> items, String[] columns){
		
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
		setModel(tableModel);
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		getColumn(columns[0]).setCellRenderer(new DefaultTableCellRenderer(){
			
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column){
				
				Component superRenderer = super.getTableCellRendererComponent(
						table, value, isSelected, hasFocus, row, column);
				
				setHorizontalAlignment(SwingConstants.CENTER);
				superRenderer.setFont(superRenderer.getFont().deriveFont(
						Font.BOLD));
				
				return superRenderer;
				
			}
			
		});
		
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
					actionOnDoubleClick();
				}
			}
		});
		
	}
	
	public JScrollPane getScrollableTable(){
		if(scrollableTable == null)
			scrollableTable = new JScrollPane(this);
		
		return scrollableTable;
	}
	
	@Override
	public abstract Object getValueAt(int rowIndex, int columnIndex);
	
	@Override
	public Class<?> getColumnClass(int columnIndex){
		return super.getColumnClass(columnIndex);
	}
	
	protected abstract void actionOnSelect(boolean hasSelection);
	
	protected abstract void actionOnDoubleClick();
	
	@Override
	public String getColumnName(int columnIndex){
		return columns[columnIndex];
	}
	
	public void addItem(TableObject newItem){
		
		items.add(newItem);
		tableModel.fireTableRowsInserted(items.size() - 1, items.size() - 1);
		
	}
	
	public void modifyItemAt(int index, TableObject modifiedObject){
		
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
	
	public void setObjects(ArrayList<TableObject> items){
		
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
	
}
