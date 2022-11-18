package com.salesinvoice.models;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class InvoicesTableModel extends AbstractTableModel{
    private ArrayList<Invoice> invoices;
    private String[] columns = {"No.", "Date", "Customer", "Total"};

    public InvoicesTableModel(ArrayList<Invoice> invoices) {
        this.invoices = invoices;
    }
    
    
    public int getRowCount() {
        return invoices.size(); 
    }
    
    public int getColumnCount() {
        return columns.length; 
    }
    
    public String getColumnName(int column) {
        return  columns[column];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Invoice invoice = invoices.get(rowIndex);
        
        switch (columnIndex){
            case 0: return invoice.getNum();
            case 1: return invoice.getDate();
            case 2: return invoice.getCustomer();
            case 3: return invoice.getInvoiceTotal();
            default: return "";
        } 
    }
}
