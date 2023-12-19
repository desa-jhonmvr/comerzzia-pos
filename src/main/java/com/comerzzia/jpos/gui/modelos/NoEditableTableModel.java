/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.modelos;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author MGRI
 */
public class NoEditableTableModel extends DefaultTableModel {

    protected int sortCol = 0;
    protected boolean isSortAsc = true;

    public NoEditableTableModel(Vector listaarticulos, Vector header) {
        super(listaarticulos, header);
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return false;
    }

    public class ColumnListener extends MouseAdapter {

        protected JTable table;

        public ColumnListener(JTable t) {
            table = t;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            TableColumnModel colModel = table.getColumnModel();
            int columnModelIndex = colModel.getColumnIndexAtX(e.getX());
            int modelIndex = colModel.getColumn(columnModelIndex).getModelIndex();
            ordenarColumna(modelIndex, table);
        }
    }

    public void ordenarColumna(int columnIndex, JTable table) {
        TableColumnModel colModel = table.getColumnModel();
        if (columnIndex < 0) {
            return;
        }
        if (sortCol == columnIndex) {
            isSortAsc = !isSortAsc;
        }
        else {
            sortCol = columnIndex;
        }
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = colModel.getColumn(i);
            column.setHeaderValue(getColumnName(column.getModelIndex()));
        }
        table.getTableHeader().repaint();
        if (isSortAsc) {
            Collections.sort(dataVector, new Comparator<Vector>() {

                @Override
                public int compare(Vector t, Vector t1) {
                    if (t.get(sortCol) instanceof String) {
                        return ((String) t.get(sortCol)).compareTo(((String) t1.get(sortCol)));
                    }
                    if (t.get(sortCol) instanceof BigDecimal) {
                        return ((BigDecimal) t.get(sortCol)).compareTo(((BigDecimal) t1.get(sortCol)));
                    }
                    else {
                        return 0;
                    }
                }
            });
        }
        else {
            Collections.sort(dataVector, new Comparator<Vector>() {

                @Override
                public int compare(Vector t, Vector t1) {
                    if (t.get(sortCol) instanceof String) {
                        return ((String) t.get(sortCol)).compareTo(((String) t1.get(sortCol)));
                    }
                    if (t.get(sortCol) instanceof BigDecimal) {
                        return ((BigDecimal) t.get(sortCol)).compareTo(((BigDecimal) t1.get(sortCol)));
                    }
                    else {
                        return 0;
                    }
                }
            });
            Collections.reverse(dataVector);
        }
        table.tableChanged(new TableModelEvent(NoEditableTableModel.this));
        table.repaint();
    }
}
