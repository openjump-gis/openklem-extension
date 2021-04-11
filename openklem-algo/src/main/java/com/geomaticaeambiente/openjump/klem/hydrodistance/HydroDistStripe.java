package com.geomaticaeambiente.openjump.klem.hydrodistance;

import com.geomaticaeambiente.openjump.klem.grid.FlowDirBasicGrid;
import com.geomaticaeambiente.openjump.klem.parallel.Shifter;
import com.geomaticaeambiente.openjump.klem.parallel.UpwardAbstractStripe;
import java.awt.Point;

/**
 *
 * @author AdL
 */
public class HydroDistStripe extends UpwardAbstractStripe {

    public HydroDistStripe(int id, FlowDirBasicGrid flowDirData, byte[][] dependencyStripe, double[][] outputData, int yOffset) {
        super(id, flowDirData, dependencyStripe, outputData, yOffset);
        
        orthoDistance = flowDirData.getCellSize();
        diagonalDistance = orthoDistance * Math.sqrt(2);
        
    }
    
    @Override
    protected void findQs () {
        
        for(int r=0; r<stripeNRows; r++) {
            for(int c=0; c<stripeNCols; c++) {                
                if(dependencyStripe[r][c] == 1 &&
                        outputData[r + yOffset][c] == 0) {
                    Qs_l.add(new java.awt.Point(c, r));
                }
            }
        }
        
    }    
    
    @Override
    public void process() {
        
        while(!Qs_l.isEmpty()) {
            
            java.awt.Point cell = Qs_l.get(Qs_l.size()-1);            
            
            Qs_l.remove(Qs_l.size()-1);
            if(flowDirsGrid.isOutlet(cell)) {
                outputData[cell.y][cell.x] = orthoDistance;
            }

            updateCell(cell);
            
            // Update dependency maps
            updateDependencyMaps(cell, 1);

        }
        
    }    
    
    @Override
    public void updateCell(Point cell)  {
        
        java.awt.Point cellGrid = new java.awt.Point(cell.x, cell.y + yOffset);
        
        if(flowDirsGrid.isOutlet(cellGrid)) {
            outputData[cellGrid.y][cellGrid.x] = orthoDistance;
            return;
        }

        /* Find the sink cell */
        int colShift = Shifter.getColShift(flowDirsGrid.getTarbotonianFlowDirValue(cellGrid));
        int rowShift = Shifter.getRowShift(flowDirsGrid.getTarbotonianFlowDirValue(cellGrid));
        java.awt.Point sinkCell = new java.awt.Point(
                cellGrid.x + colShift,
                cellGrid.y + rowShift);
        
        if(colShift * rowShift == 0) {
            outputData[cellGrid.y][cellGrid.x] =
                    outputData[sinkCell.y][sinkCell.x] + orthoDistance;
        } else {
            outputData[cellGrid.y][cellGrid.x] =
                    outputData[sinkCell.y][sinkCell.x] + diagonalDistance;                    
        }        
        
    }
    
    private final double orthoDistance;
    private final double diagonalDistance;
    
}
