package com.salesinvoice.controller;

import com.salesinvoice.models.Invoice;
import com.salesinvoice.models.InvoicesTableModel;
import com.salesinvoice.models.Line;
import com.salesinvoice.models.LinesTableModel;
import com.salesinvoice.views.InvoiceDialog;
import com.salesinvoice.views.MainFrame;
import com.salesinvoice.views.LineDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Controller implements ActionListener, ListSelectionListener {

    private MainFrame frame;
    private InvoiceDialog invoiceDialog;
    private LineDialog lineDialog;

    public Controller(MainFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        String actionCommand = e.getActionCommand();
        System.out.println("Action: " + actionCommand);
            switch (actionCommand) {
                case "Load File":
                    loadFileMenuBar();
                    break;
                case "Save File":
                    saveFileMenuBar();
                    break;
                case "Create New Invoice":
                    createNewInvoiceButton();
                    break;
                case "Delete Invoice":
                    deleteInvoiceButton();
                    break;
                case "Create New Item":
                    createNewItemButton();
                    break;
                case "Delete Item":
                    deleteItemButton();
                    break;
                case "createInvoiceAdd":
                    createInvoiceAddButton();
                    break;
                case "createInvoiceCancel":
                    createInvoiceCancelButton();
                    break;
                case "createLineAdd":
                    createLineAddButton();
                    break;
                case "createLineCancel":
                    createLineCancelButton();
                    break;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        
        int selectedIndex = frame.getInvoiceTable().getSelectedRow();
        if (selectedIndex != -1) {
            System.out.println("You have selected row: " + selectedIndex);
            Invoice currentInvoice = frame.getInvoices().get(selectedIndex);
            frame.getInoviceNumberLabel().setText("" + currentInvoice.getNum());
            frame.getInvoiceDateText().setText(currentInvoice.getDate());
            frame.getCustomerNameText().setText(currentInvoice.getCustomer());
            frame.getInvoiceTotalLabel().setText("" + currentInvoice.getInvoiceTotal());
            LinesTableModel linesTableModel = new LinesTableModel(currentInvoice.getLines());
            frame.getLineTable().setModel(linesTableModel);
            linesTableModel.fireTableDataChanged();
        }
    }

    private void loadFileMenuBar() {
        
        JFileChooser fc = new JFileChooser();
        try {
            int result = fc.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                Path headerPath = Paths.get(headerFile.getAbsolutePath());
                List<String> headerLines = Files.readAllLines(headerPath);
                System.out.println("InvoicesFile read");
                ArrayList<Invoice> invoicesArray = new ArrayList<>();
                for (String headerLine : headerLines) {
                    try {
                        String[] headerParts = headerLine.split(",");
                        int invoiceNum = Integer.parseInt(headerParts[0]);
                        String invoiceDate = headerParts[1];
                        String customerName = headerParts[2];

                        Invoice invoice = new Invoice(invoiceNum, invoiceDate, customerName);
                        invoicesArray.add(invoice);
                    } catch (Exception exception) {
                            exception.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Error in file format", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                result = fc.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fc.getSelectedFile();
                    Path linePath = Paths.get(lineFile.getAbsolutePath());
                    List<String> lineLines = Files.readAllLines(linePath);
                    System.out.println("LinesFile read");
                    for (String lineLine : lineLines) {
                        try {
                            String lineParts[] = lineLine.split(",");
                            int invoiceNum = Integer.parseInt(lineParts[0]);
                            String itemName = lineParts[1];
                            double itemPrice = Double.parseDouble(lineParts[2]);
                            int count = Integer.parseInt(lineParts[3]);
                            Invoice inv = null;
                            for (Invoice invoice : invoicesArray) {
                                if (invoice.getNum() == invoiceNum) {
                                    inv = invoice;
                                    break;
                                }
                            }

                            Line line = new Line(itemName, itemPrice, count, inv);
                            inv.getLines().add(line);
                        } catch (Exception exception) {
                                exception.printStackTrace();
                                JOptionPane.showMessageDialog(frame, "Error in file format", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                frame.setInvoices(invoicesArray);
                InvoicesTableModel invoicesTableModel = new InvoicesTableModel(invoicesArray);
                frame.setInvoicesTableModel(invoicesTableModel);
                frame.getInvoiceTable().setModel(invoicesTableModel);
                frame.getInvoicesTableModel().fireTableDataChanged();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Cannot read file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveFileMenuBar() {
        
        ArrayList<Invoice> invoices = frame.getInvoices();
        String headers = "";
        String lines = "";
        for (Invoice invoice : invoices) {
            String invoiceFileFormat = invoice.getFileFormat();
            headers += invoiceFileFormat;
            headers += "\n";

            for (Line line : invoice.getLines()) {
                String lineFileFormat = line.getFileFormat();
                lines += lineFileFormat;
                lines += "\n";
            }
        }
        try {
            JFileChooser fc = new JFileChooser();
            int result = fc.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                FileWriter headerFileWriter = new FileWriter(headerFile);
                headerFileWriter.write(headers);
                headerFileWriter.flush();
                headerFileWriter.close();
                result = fc.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fc.getSelectedFile();
                    FileWriter lineFileWriter = new FileWriter(lineFile);
                    lineFileWriter.write(lines);
                    lineFileWriter.flush();
                    lineFileWriter.close();
                }
            }
        } catch (Exception exception) {
        }
    }

    private void createNewInvoiceButton() {
        
        invoiceDialog = new InvoiceDialog(frame);
        invoiceDialog.setVisible(true);
    }

    private void deleteInvoiceButton() {
        
        int selectedRow = frame.getInvoiceTable().getSelectedRow();
        if (selectedRow != -1) {
            frame.getInvoices().remove(selectedRow);
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
    }

    private void createNewItemButton() {
        
        lineDialog = new LineDialog(frame);
        lineDialog.setVisible(true);
    }

    private void deleteItemButton() {
        
        int selectedRow = frame.getLineTable().getSelectedRow();

        if (selectedRow != -1) {
            LinesTableModel linesTableModel = (LinesTableModel) frame.getLineTable().getModel();
            linesTableModel.getLines().remove(selectedRow);
            linesTableModel.fireTableDataChanged();
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
    }

    private void createInvoiceCancelButton() {
        
        invoiceDialog.setVisible(false);
        invoiceDialog.dispose();
        invoiceDialog = null;
    }

    private void createInvoiceAddButton() {
        
        String date = invoiceDialog.getInvoiceDtField().getText();
        String customer = invoiceDialog.getCustomerNameField().getText();
        int num = frame.getNextInvoiceNumber();
        try {
            String[] dateParts = date.split("-");
            if (dateParts.length < 3) {
                JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
            } 
                else {
                    int day = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]);
                    int year = Integer.parseInt(dateParts[2]);
                    if (day > 31 || month > 12) {
                        JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
                } 
                        else {
                        Invoice invoice = new Invoice(num, date, customer);
                        frame.getInvoices().add(invoice);
                        frame.getInvoicesTableModel().fireTableDataChanged();
        
                        invoiceDialog.setVisible(false);
                        invoiceDialog.dispose();
                        invoiceDialog = null;
                }
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void createLineAddButton() {
        
        String item = lineDialog.getItemNameField().getText();
        String countStr = lineDialog.getItemCountField().getText();
        String priceStr = lineDialog.getItemPriceField().getText();
        int count = Integer.parseInt(countStr);
        double price = Double.parseDouble(priceStr);
        int selectedInvoice = frame.getInvoiceTable().getSelectedRow();
        if (selectedInvoice != -1) {
            Invoice invoice = frame.getInvoices().get(selectedInvoice);
            Line line = new Line(item, price, count, invoice);
            invoice.getLines().add(line);
            LinesTableModel linesTableModel = (LinesTableModel) frame.getLineTable().getModel();
            linesTableModel.fireTableDataChanged();
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
        
        
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

    private void createLineCancelButton() {
        
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

}
