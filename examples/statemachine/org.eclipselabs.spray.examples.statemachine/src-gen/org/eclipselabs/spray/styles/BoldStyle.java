/**
 * This is a generated Style class for Spray.
 */
package org.eclipselabs.spray.styles;

import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.algorithms.styles.Style;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;
import org.eclipselabs.spray.styles.DefaultSprayStyle;

/**
 * This is a generated Style class for Spray.
 * Description: styling applying to all kind of special nodes
 */
@SuppressWarnings("all")
public class BoldStyle extends DefaultSprayStyle {
    
    /**
	 * This method creates a Style and returns the defined style.
	 * Description: styling applying to all kind of special nodes
	 */
    @Override
	public Style newStyle(Diagram diagram) {
		IGaService gaService = Graphiti.getGaService();
		
		// Creating Style with given id and description
		Style style = super.newStyle(diagram);
		style.setId("BoldStyle");
		style.setDescription("styling applying to all kind of special nodes");
		
		// transparency value
		
		// background attributes
		
		// line attributes
		
		// font attributes
		String fontName = style.getFont().getName();
		int fontSize = 8;
		boolean fontItalic = style.getFont().isItalic();
		boolean fontBold = true;
		style.setFont(gaService.manageFont(diagram, fontName, fontSize, fontItalic, fontBold));
		return style;
	}
	
    /**
	 * This method returns the font color for the style. 
	 * The font color will be returned separated, because Graphiti allows just the foreground color.
	 * The foreground color will be used for lines and fonts at the same time.
	 */
	@Override
	public Color getFontColor(Diagram diagram) {
		return super.getFontColor(diagram);
	}
	
}	
