package com.salesinvoice.views;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class InvoiceDialog extends JDialog {
    private JTextField invoiceDtField;    
    private JTextField customerField;
    private JLabel invoiceDtLabel;    
    private JLabel customerLabel;
    private JButton addButton;
    private JButton cancelButton;

    public InvoiceDialog(MainFrame frame) {
        invoiceDtLabel = new JLabel("Invoice Date:");
        invoiceDtField = new JTextField(15); 
        
        customerLabel = new JLabel("Customer Name:");
        customerField = new JTextField(15);
        
        addButton = new JButton("Add");
        cancelButton = new JButton("Cancel");
        
        addButton.setActionCommand("createInvoiceAdd");
        cancelButton.setActionCommand("createInvoiceCancel");
        
        addButton.addActionListener(frame.getController());
        cancelButton.addActionListener(frame.getController());
        setLayout(new GridLayout(3, 3));
        
        add(invoiceDtLabel);
        add(invoiceDtField);
        add(customerLabel);
        add(customerField);
        add(addButton);
        add(cancelButton);
        
        pack();
        
    }

    public JTextField getCustomerNameField() {
        return customerField;
    }

    public JTextField getInvoiceDtField() {
        return invoiceDtField;
    }
    
}
