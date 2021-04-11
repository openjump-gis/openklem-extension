package com.geomaticaeambiente.klemgui.ui;

import com.geomaticaeambiente.klemgui.utils.PluginUtils;
import com.geomaticaeambiente.klemgui.utils.PersonalTable;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import java.awt.Color;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 * Class that builds a JPanel with a table for reclassify a raster file.
 *
 * @author Geomatica
 */
public class PersonalTableComponents extends javax.swing.JPanel {

    
    public PersonalTableComponents(PersonalTable personalTable, FileNameExtensionFilter fileNameExtFilter) {
        
        this.personalTable = personalTable;
        this.fileNameExtFilter = fileNameExtFilter;
        
        initComponents();
        fixComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_values = new javax.swing.JTable();
        jButton_RemoveRows = new javax.swing.JButton();
        jButton_AddRow = new javax.swing.JButton();
        jButton_Load = new javax.swing.JButton();
        jButton_Save = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(380, 200));
        setMinimumSize(new java.awt.Dimension(380, 200));
        setPreferredSize(new java.awt.Dimension(380, 200));
        setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setAutoscrolls(true);

        jTable_values.setAutoCreateRowSorter(true);
        jTable_values.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        jTable_values.setMaximumSize(new java.awt.Dimension(300, 200));
        jTable_values.setMinimumSize(new java.awt.Dimension(300, 200));
        jScrollPane1.setViewportView(jTable_values);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 350;
        gridBagConstraints.ipady = 129;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        add(jScrollPane1, gridBagConstraints);

        jButton_RemoveRows.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/geomaticaeambiente/klemgui/images/cross.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/geomaticaeambiente/klemgui/resources/Bundle"); // NOI18N
        jButton_RemoveRows.setToolTipText(bundle.getString("PersonalTableComponents.jButton_RemoveRows.text")); // NOI18N
        jButton_RemoveRows.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton_RemoveRows.setContentAreaFilled(false);
        jButton_RemoveRows.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton_RemoveRows.setMaximumSize(new java.awt.Dimension(50, 23));
        jButton_RemoveRows.setMinimumSize(new java.awt.Dimension(50, 23));
        jButton_RemoveRows.setPreferredSize(new java.awt.Dimension(50, 23));
        jButton_RemoveRows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_RemoveRowsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 5, 5);
        add(jButton_RemoveRows, gridBagConstraints);

        jButton_AddRow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/geomaticaeambiente/klemgui/images/add.png"))); // NOI18N
        jButton_AddRow.setToolTipText(bundle.getString("PersonalTableComponents.jButton_AddRow.text")); // NOI18N
        jButton_AddRow.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton_AddRow.setContentAreaFilled(false);
        jButton_AddRow.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton_AddRow.setMaximumSize(new java.awt.Dimension(50, 23));
        jButton_AddRow.setMinimumSize(new java.awt.Dimension(50, 23));
        jButton_AddRow.setPreferredSize(new java.awt.Dimension(50, 23));
        jButton_AddRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AddRowActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 5, 5);
        add(jButton_AddRow, gridBagConstraints);

        jButton_Load.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/geomaticaeambiente/klemgui/images/folder_page.png"))); // NOI18N
        jButton_Load.setToolTipText(bundle.getString("PersonalTableComponents1.jButton_Load.text")); // NOI18N
        jButton_Load.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton_Load.setContentAreaFilled(false);
        jButton_Load.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton_Load.setMaximumSize(new java.awt.Dimension(50, 23));
        jButton_Load.setMinimumSize(new java.awt.Dimension(50, 23));
        jButton_Load.setPreferredSize(new java.awt.Dimension(50, 23));
        jButton_Load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_LoadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 5, 5);
        add(jButton_Load, gridBagConstraints);

        jButton_Save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/geomaticaeambiente/klemgui/images/disk.png"))); // NOI18N
        jButton_Save.setToolTipText(bundle.getString("KlemGUI.SaveButton.label")); // NOI18N
        jButton_Save.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton_Save.setContentAreaFilled(false);
        jButton_Save.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton_Save.setMaximumSize(new java.awt.Dimension(50, 23));
        jButton_Save.setMinimumSize(new java.awt.Dimension(50, 23));
        jButton_Save.setPreferredSize(new java.awt.Dimension(50, 23));
        jButton_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 5, 5);
        add(jButton_Save, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_RemoveRowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_RemoveRowsActionPerformed

        GUIUtils.removeRow(personalTable.getDefaultTableModel(), jTable_values);
        
    }//GEN-LAST:event_jButton_RemoveRowsActionPerformed

    private void jButton_AddRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AddRowActionPerformed

        if(personalTable.addFile()) {
            
            FileNameExtensionFilter filter = null;
            String fileDesc = "File";
            if(personalTable.fileDesc() != null) {
                fileDesc = personalTable.fileDesc();
            }
            if(personalTable.extensions() != null) {
                filter = new FileNameExtensionFilter(
                        fileDesc, personalTable.extensions());
            }
            
            File[] files = PluginUtils.openJChooserDialog(
                    this,
                    JFileChooser.FILES_ONLY, 
                    JFileChooser.OPEN_DIALOG,
                    filter,
                    null,
                    personalTable.multipleFiles());
            
            if(files != null) {
                for(File file : files) {
                    personalTable.getDefaultTableModel().addRow(new Object[]{null});
                    int lastRow = personalTable.getDefaultTableModel().getRowCount() - 1;
                    personalTable.getDefaultTableModel().setValueAt(file.getAbsolutePath(), lastRow, 0);
                }
            }
                
        } else {
            GUIUtils.addRows(personalTable.getDefaultTableModel(), jTable_values);
        }
        
    }//GEN-LAST:event_jButton_AddRowActionPerformed

    private void jButton_LoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_LoadActionPerformed

        try {
            File[] files = PluginUtils.openJChooserDialog(
                    this,
                    JFileChooser.FILES_ONLY,
                    JFileChooser.OPEN_DIALOG,
                    fileNameExtFilter,
                    PluginUtils.getWorkspacePath(), 
                    personalTable.multipleFiles());
            
            if(files == null) return;
            
            personalTable.loadFromFile(files[0]);
            
        } catch (Exception ex) {
            ErrorDialog.show(
                    this,
                    PluginUtils.plugInName,
                    ex.toString(),
                    StringUtil.stackTrace(ex));
        }
        
        
    }//GEN-LAST:event_jButton_LoadActionPerformed

    private void jButton_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SaveActionPerformed

        try {
            
            if(jTable_values.isEditing()){
                jTable_values.getCellEditor().stopCellEditing();
            }
            
            //Save file
            File file = PluginUtils.openJChooserDialog(
                    null, 
                    JFileChooser.FILES_ONLY, 
                    JFileChooser.SAVE_DIALOG,
                    fileNameExtFilter,
                    null, false)[0];
            if(file == null) return;
            
            personalTable.saveTable(file);
        
        } catch (Exception ex){
            ErrorDialog.show(
                    this,
                    PluginUtils.plugInName,
                    ex.toString(),
                    StringUtil.stackTrace(ex));
        }

    }//GEN-LAST:event_jButton_SaveActionPerformed

    private void fixComponents() {
        jButton_Load.setVisible(personalTable.loadIsVisible());
        jButton_Save.setVisible(personalTable.saveIsVisible());
        jButton_AddRow.setVisible(personalTable.addIsVisible());
        jButton_RemoveRows.setVisible(personalTable.removeIsVisible());
        jTable_values.setModel(personalTable.getDefaultTableModel());
    }
    

    private void setCellRenderer(int row) {

        YourTableCellRenderer1 ytcr = new YourTableCellRenderer1();
        ytcr.setRow(row);
        ytcr.setForeColour(Color.BLUE);
        for (int c = 0; c < jTable_values.getColumnCount(); c++) {
            jTable_values.getColumnModel().getColumn(c).setCellRenderer(ytcr);
        }

    }
        
    
    public DefaultTableModel getTabelModel() {
        return personalTable.getDefaultTableModel();
    }
        
    public JTable getTabel() {
        return jTable_values;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_AddRow;
    private javax.swing.JButton jButton_Load;
    private javax.swing.JButton jButton_RemoveRows;
    private javax.swing.JButton jButton_Save;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_values;
    // End of variables declaration//GEN-END:variables

    private final FileNameExtensionFilter fileNameExtFilter;
    private final PersonalTable personalTable;

    public static void main(String[] args) {

////        JDialog dialog = new JDialog();
////        dialog.setLayout(new FlowLayout());
////
////        PersonalTableComponents rc = new PersonalTableComponents();
////        dialog.add(rc);
////        dialog.setMinimumSize(new Dimension(400, 300));
////        dialog.setPreferredSize(new Dimension(400, 300));
////        dialog.setVisible(true);

    }
}