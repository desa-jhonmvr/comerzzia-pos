/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui.modelos.foco;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.Vector;

/**
 *
 * @author MGRI
 */
public class POSFocusTraversalPolicy extends FocusTraversalPolicy {
    Vector<Component> order;

    public POSFocusTraversalPolicy(Vector<Component> order) {
      this.order = new Vector<Component>(order.size());
      this.order.addAll(order);
    }
    
    public POSFocusTraversalPolicy() {
      
    }

    public Component getComponentAfter(Container focusCycleRoot,
        Component aComponent) {
      int idx = (order.indexOf(aComponent) + 1) % order.size();
      return order.get(idx);
    }
    
    public Component getComponentBefore(Container focusCycleRoot,
        Component aComponent) {
      int idx = order.indexOf(aComponent) - 1;
      if (idx < 0) {
        idx = order.size() - 1;
      }
      return order.get(idx);
    }

    public Component getDefaultComponent(Container focusCycleRoot) {
      return order.get(0);
    }

    public Component getLastComponent(Container focusCycleRoot) {
      return order.lastElement();
    }

    public Component getFirstComponent(Container focusCycleRoot) {
      return order.get(0);
    }
  }
    

